package com.tvsc.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.tvsc.service.Service
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver

/**
 *
 * @author Taras Zubrei
 */
@EnableWebMvc
@Configuration
@EnableAutoConfiguration
@Import(Service.class)
@ComponentScan("com.tvsc.web")
class Web extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(Web.class, args);
    }

    @Bean
    LocaleResolver localeResolver() {
        new AcceptHeaderLocaleResolver(defaultLocale: new Locale('en'))
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        new ResourceBundleMessageSource(basename: 'i18n.messages', defaultEncoding: 'utf-8')
    }

    @Autowired
    ObjectMapper objectMapper

    @Override
    void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper))
    }


    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.registerDeferredResultInterceptors(
                new DeferredResultProcessingInterceptorAdapter() {
                    @Override
                    public <T> boolean handleTimeout(NativeWebRequest request, DeferredResult<T> result) {
                        result.setErrorResult(new ServiceUnavailableException());
                        return false;
                    }
                });
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    static class ServiceUnavailableException extends Exception {}


    @Component
    class ModelMapperFactoryBean implements FactoryBean<ModelMapper> {
        @Override
        public ModelMapper getObject() throws Exception {
            ModelMapper modelMapper = new ModelMapper()
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
            modelMapper
        }

        @Override
        public Class<?> getObjectType() {
            ModelMapper
        }

        @Override
        public boolean isSingleton() {
            true
        }
    }
}
