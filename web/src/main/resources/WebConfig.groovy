import com.tvsc.core.AppProfiles
import com.tvsc.service.config.ServiceConfig
import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.core.DefaultParameterNameDiscoverer
import org.springframework.core.io.ClassPathResource
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

beans {
    importBeans(ServiceConfig)
    xmlns([context: 'http://www.springframework.org/schema/context'])
    context.'component-scan'('base-package': 'com.tvsc.web')

    modelMapper(ModelMapper) {
        getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT)
    }

    if (environment.activeProfiles.contains(AppProfiles.TEST)) {
        requestMappingHandlerMapping = new RequestMappingHandlerMapping()
        parameterNameDiscoverer = new DefaultParameterNameDiscoverer()
        webTestProperties(PropertiesFactoryBean) {
            location = new ClassPathResource("web-test.properties")
        }
    }
}