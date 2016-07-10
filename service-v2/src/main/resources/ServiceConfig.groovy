import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.tvsc.service.Constants
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES

def String getToken() {
    return new ObjectMapper().readTree(new OkHttpClient.Builder().build()
            .newCall(new Request.Builder()
            .url("$Constants.API/login")
            .post(RequestBody.create(MediaType.parse('application/json'), """{"apikey":"$Constants.API_KEY"}"""))
            .build()).execute().body().string()).get('token').asText()
}

Logger LOGGER = LoggerFactory.getLogger(this.class)

def retryRequest = {
    chain ->
        Request request = chain.request();
        // try the request
        Response response = chain.proceed(request);
        int tryCount = 0;
        while (!response.isSuccessful() && tryCount < 5) {
            new OkHttpClient.Builder().build().newCall(new Request.Builder().url("${Constants.API}refresh_token").build()).execute();
            LOGGER.debug('Token refreshed');
            tryCount++;
            // retry the request
            response = chain.proceed(request);
        }
        // otherwise just pass the original response on
        return response;
}

beans {
    xmlns([context: 'http://www.springframework.org/schema/context'])
    context.'component-scan'('base-package': 'com.tvsc.v2.service')
    importBeans('classpath*:PersistenceConfig.groovy')

    objectMapper = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(new JavaTimeModule())
            .registerModule(new KotlinModule())

    okHttpClient = new OkHttpClient.Builder().addInterceptor {
        it.proceed(it.request().newBuilder()
                .addHeader('Authorization', "Bearer ${getToken()}")
                .addHeader('Accept-Language', 'en') //TODO: change language
                .build())
    }
    .addInterceptor(retryRequest).build();
}