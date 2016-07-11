import com.tvsc.core.AppProfiles
import com.tvsc.service.config.ServiceConfig
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

beans {
    importBeans(ServiceConfig)
    xmlns([context: 'http://www.springframework.org/schema/context'])
    context.'component-scan'('base-package': 'com.tvsc.web')

    modelMapper(ModelMapper) {
        getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
    }
    messageSource(ResourceBundleMessageSource) {
        basename = 'i18n.messages'
        defaultEncoding = 'utf-8'
    }
    localeResolver(AcceptHeaderLocaleResolver)
    configureContentNegotiation(ContentNegotiationConfigurer) {
        defaultContentType(MediaType.APPLICATION_JSON).ignoreAcceptHeader(true)
    }

    if (environment.activeProfiles.contains(AppProfiles.TEST)) {
        requestMappingHandlerMapping = new RequestMappingHandlerMapping()
        parameterNameDiscoverer = new DefaultParameterNameDiscoverer()
        webTestProperties(PropertiesFactoryBean) {
            location = new ClassPathResource("web-test.properties")
        }
    }
}