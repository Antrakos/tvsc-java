package com.tvsc.web.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.tvsc.core.AppProfiles
import com.tvsc.service.config.ServiceConfig
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.io.ClassPathResource
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

/**
 * @author Taras Zubrei
 */
@Configuration
@EnableWebMvc
@Import(ServiceConfig)
@ComponentScan("com.tvsc.web")
class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    def objectMapper() {
        new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .indentOutput(true)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build()
    }

    @Bean
    @Profile(AppProfiles.TEST)
    def requestMappingHandlerMapping() {
        new RequestMappingHandlerMapping()
    }

    @Bean
    @Profile(AppProfiles.TEST)
    def parameterNameDiscoverer() {
        new DefaultParameterNameDiscoverer()
    }

    @Bean
    @Profile(AppProfiles.TEST)
    def webTestProperties() {
        new PropertiesFactoryBean(locations: new ClassPathResource("web-test.properties"))
    }
}