package com.tvsc.service.util

import com.tvsc.core.AppProfiles
import com.tvsc.service.Constants
import com.tvsc.service.config.ServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = ServiceConfig)
@ActiveProfiles(AppProfiles.TEST)
class HttpUtilsSpecification extends Specification {
    @Autowired
    HttpUtils httpUtils

    def "given CompletableFuture of String when get String response then it is not empty"() {
        given:
        def single = httpUtils.get("$Constants.SERIES/78901")
        when:
        def response = single.toBlocking().value()
        then:
        !response.empty
    }
}
