package com.tvsc.service.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.tvsc.persistence.config.PersistenceConfig
import com.tvsc.service.Constants
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.DefaultAsyncHttpClient
import org.asynchttpclient.DefaultAsyncHttpClientConfig
import org.asynchttpclient.RequestBuilder
import org.asynchttpclient.filter.FilterContext
import org.asynchttpclient.filter.RequestFilter
import org.asynchttpclient.filter.ResponseFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.util.*

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

    val token: String = ObjectMapper().readTree(
            DefaultAsyncHttpClient()
                    .preparePost("${Constants.API}/login")
                    .addHeader("content-type", "application/json")
                    .setBody("""{"apikey":"${Constants.API_KEY}"}""")
                    .execute()
                    .get()
                    .responseBody
    ).get("token").asText().let { LOGGER.info("Token: {}", it); it }

    @Bean
    open fun objectMapper(): ObjectMapper = ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(JavaTimeModule())

    @Bean(destroyMethod = "close")
    open fun asyncHttpClient(): AsyncHttpClient = DefaultAsyncHttpClientConfig.Builder()
            .addRequestFilter(object : RequestFilter {
                override fun <K> filter(ctx: FilterContext<K>): FilterContext<K> = ctx.apply {
                    request.headers
                            .add("Authorization", String.format("Bearer %s", token))
                            .add("Accept-Language", "en")
                }
            })
            .addResponseFilter(object : ResponseFilter {
                var previousRequest: Optional<org.asynchttpclient.Request> = Optional.empty()
                override fun <K> filter(ctx: FilterContext<K>): FilterContext<K> {
                    if (ctx.responseStatus.statusCode == 503) {
                        previousRequest = Optional.of(ctx.request)
                        return FilterContext.FilterContextBuilder(ctx)
                                .request(RequestBuilder("GET").setUrl(Constants.API + "/refresh_token").build())
                                .build()
                    } else if (previousRequest.isPresent) {
                        return FilterContext.FilterContextBuilder(ctx)
                                .request(previousRequest.get())
                                .build().let { previousRequest = Optional.empty(); it }
                    }
                    return ctx
                }
            })
            .build()
            .let { DefaultAsyncHttpClient(it) }
}