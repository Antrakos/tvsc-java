package com.tvsc.service.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.tvsc.persistence.config.PersistenceConfig
import com.tvsc.service.Constants
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.util.concurrent.TimeUnit

/**
 *
 * @author Taras Zubrei
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.tvsc.service")
@Import(PersistenceConfig::class)
open class ServiceConfig {
    val LOGGER: Logger = LoggerFactory.getLogger(ServiceConfig::class.java)
    @Bean
    open fun okHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor {
        it.proceed(it.request().newBuilder()
                .addHeader("Authorization", "Bearer " + getToken())
                .addHeader("Accept-Language", "en") //TODO: change language
                .build())
    }.addInterceptor(retryRequest)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

    @Bean
    open fun objectMapper(): ObjectMapper = ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(JavaTimeModule())

    private val retryRequest: (Interceptor.Chain) -> Response = {
        chain ->
        val request = chain.request()
        // try the request
        val originalResponse = chain.proceed(request)
        var response = originalResponse
        var tryCount = 0
        while (response.code() == 403 && tryCount < 5) {
            OkHttpClient.Builder().build().newCall(Request.Builder().url("${Constants.API}/refresh_token").build()).execute()
            LOGGER.debug("Token refreshed")
            tryCount++
            // retry the request
            response = chain.proceed(request)
        }
        // otherwise just pass the original response on
        originalResponse
    }

    private fun getToken(): String {
        return ObjectMapper().readTree(OkHttpClient.Builder().build()
                .newCall(Request.Builder()
                        .url("${Constants.API}/login")
                        .post(RequestBody.create(MediaType.parse("application/json"), """{"apikey":"${Constants.API_KEY}"}"""))
                        .build())
                .execute().body().string()).get("token").asText()
    }
}