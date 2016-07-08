package com.tvsc.service

import com.tvsc.core.AppProfiles
import com.tvsc.core.model.Serial
import com.tvsc.service.config.ServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = ServiceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
class SerialServiceSpecification extends Specification {
    @Autowired
    private serialService;

    def "when get serial info then check if it contains only general info"() {
        when:
        Serial serialInfo = serialService.getSerialInfo(78901L);
        then:
        serialInfo.seasons == null
    }

    def "when restore all data then check if data presents"() {
        when:
        List<Serial> series = serialService.restoreAllData();
        then:
        !series.isEmpty()
    }
}
