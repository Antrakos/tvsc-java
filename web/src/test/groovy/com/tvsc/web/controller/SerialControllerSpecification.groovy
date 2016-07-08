package com.tvsc.web.controller

import com.tvsc.core.AppProfiles
import com.tvsc.web.SerialDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration('classpath*:WebConfig.groovy')
@ActiveProfiles(AppProfiles.TEST)
class SerialControllerSpecification extends Specification {
    @Autowired
    SerialController serialController

    def "ad"() {
        when:
        List<SerialDto> data = serialController.findAll()
        then:
        println(data)
    }
}
