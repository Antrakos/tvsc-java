package com.tvsc.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.tvsc.core.exception.ExceptionUtil;
import com.tvsc.service.Constants;
import com.tvsc.service.exception.HttpException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.filter.FilterContext;
import org.asynchttpclient.filter.FilterException;
import org.asynchttpclient.filter.RequestFilter;
import org.asynchttpclient.filter.ResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.io.IOException;
import java.util.Arrays;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author Taras Zubrei
 */
@Configuration
@ImportResource("classpath*:PersistenceConfig.groovy")
@ComponentScan("com.tvsc.service")
public class ServiceConfig {
    private Logger LOGGER = LoggerFactory.getLogger(ServiceConfig.class);
    private String token;

    private String getToken() throws IOException {
        if (token != null)
            return token;
        HttpPost authorizationRequest = new HttpPost(Constants.API + "login");
        authorizationRequest.setEntity(new StringEntity("{\"apikey\":\"" + Constants.API_KEY + "\"}", ContentType.APPLICATION_JSON));
        try (CloseableHttpClient client = HttpClientBuilder.create().build();
             CloseableHttpResponse authorizationResponse = client.execute(authorizationRequest)) {
            authorizationRequest.releaseConnection();
            token = new ObjectMapper().readTree(authorizationResponse.getEntity().getContent()).get("token").asText();
            LOGGER.debug("token=" + token);
            return token;
        }
    }

    @Bean(destroyMethod = "close")
    public CloseableHttpClient httpClient() throws IOException {
        return HttpClientBuilder
                .create()
                .setServiceUnavailableRetryStrategy(new RefreshTokenRetryStrategy())
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader("Authorization", String.format("Bearer %s", getToken())),
                        new BasicHeader("Accept-Language", "en"))) //TODO: change language
                .build();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                        .addHeader("Authorization", String.format("Bearer %s", getToken()))
                        .addHeader("Accept-Language", "en") //TODO: change language
                        .build()))
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    // try the request
                    Response response = chain.proceed(request);
                    int tryCount = 0;
                    while (!response.isSuccessful() && tryCount < 5) {
                        new OkHttpClient.Builder().build().newCall(new Request.Builder().url(Constants.API + "refresh_token").build()).execute();
                        LOGGER.debug("Token refreshed");
                        tryCount++;
                        // retry the request
                        response = chain.proceed(request);
                    }
                    // otherwise just pass the original response on
                    return response;
                })
                .build();
    }

    @Bean(destroyMethod = "close")
    public AsyncHttpClient asyncHttpClient() {
        DefaultAsyncHttpClientConfig clientConfig = new DefaultAsyncHttpClientConfig.Builder()
                .addRequestFilter(new RequestFilter() {
                    @Override
                    public <T> FilterContext<T> filter(FilterContext<T> ctx) throws FilterException {
                        ExceptionUtil.wrapCheckedException(new HttpException("Cannot add headers"), () -> {
                            ctx.getRequest().getHeaders()
                                    .add("Authorization", String.format("Bearer %s", getToken()))
                                    .add("Accept-Language", "en"); //TODO: change language
                            return null;
                        });
                        return ctx;
                    }
                }).addResponseFilter(new ResponseFilter() {
                    public <T> FilterContext<T> filter(FilterContext<T> ctx) throws FilterException {
                        if (ctx.getResponseStatus().getStatusCode() == 503) {
                            return new FilterContext.FilterContextBuilder<T>(ctx)
                                    .request(new RequestBuilder("GET").setUrl(Constants.API + "refresh_token").build())
                                    .build();
                        }
                        return ctx;
                    }
                })
                .build();
        return new DefaultAsyncHttpClient(clientConfig);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                .registerModule(new JavaTimeModule())
                .registerModule(new KotlinModule());
    }


    private class RefreshTokenRetryStrategy implements ServiceUnavailableRetryStrategy {
        @Override
        public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 401) {
                try {
                    HttpGet refresh = new HttpGet(Constants.API + "refresh_token");
                    HttpClientBuilder.create().build().execute(refresh).close();
                    refresh.releaseConnection();
                    LOGGER.debug("Token refreshed");
                    return executionCount < 5;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        public long getRetryInterval() {
            return 0;
        }
    }
}
