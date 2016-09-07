package com.tvsc.service.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.rx.rx_response
import com.github.kittinunf.fuel.rx.rx_string
import com.tvsc.persistence.config.PersistenceConfig
import com.tvsc.service.Constants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.nio.charset.Charset

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

    val token: String = Constants.LOGIN.httpPost()
            .header("content-type".to("application/json"))
            .body("""{"apikey":"${Constants.API_KEY}"}""", Charset.defaultCharset())
            .rx_string(Charset.defaultCharset())
            .toSingle()
            .map { ObjectMapper().readTree(it).get("token").asText() }
            .doOnSuccess { LOGGER.info("Token: {}", it) }
            .toBlocking().value()

    @Bean
    open fun objectMapper(): ObjectMapper = ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(JavaTimeModule())

    @Bean
    open fun fuel(): FuelManager {
        val fuelManager = FuelManager.instance
        fuelManager.baseHeaders = mapOf("Authorization" to String.format("Bearer %s", token), "Accept-Language" to "en")
        fuelManager.addResponseInterceptor { next: (Request, Response) -> Response ->
            { req: Request, res: Response ->
                if (res.httpStatusCode == 503) {
                    fuelManager.request(Method.GET, Constants.REFRESH_TOKEN)
                            .rx_response()
                            .toSingle()
                            .toBlocking()
                            .value()
                }
                next(req, res)
            }
        }
        return fuelManager
    }
}