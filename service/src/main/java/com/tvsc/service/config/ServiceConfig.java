package com.tvsc.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.tvsc.persistence.config.PersistenceConfig;
import com.tvsc.service.Constants;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.util.Arrays;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author Taras Zubrei
 */
@Configuration
@Import(PersistenceConfig.class)
@ComponentScan("com.tvsc.service")
public class ServiceConfig {
    private Logger LOGGER = LoggerFactory.getLogger(ServiceConfig.class);

    @Bean
    public HttpClient httpClient() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost authorizationRequest = new HttpPost(Constants.API + "login");
        authorizationRequest.setEntity(new StringEntity("{\"apikey\":\"" + Constants.API_KEY + "\"}", ContentType.APPLICATION_JSON));
        CloseableHttpResponse authorizationResponse = client.execute(authorizationRequest);
        authorizationRequest.releaseConnection();
        String token = new ObjectMapper().readTree(authorizationResponse.getEntity().getContent()).get("token").asText();
        authorizationResponse.close();
        client.close();

        LOGGER.debug("token=" + token);
        return HttpClientBuilder
                .create()
                .setServiceUnavailableRetryStrategy(new RefreshTokenRetryStrategy())
                .setDefaultHeaders(Arrays.asList(
                        new BasicHeader("Authorization", String.format("Bearer %s", token)),
                        new BasicHeader("Accept-Language", "en"))) //TODO: change language
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new KotlinModule());
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
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
