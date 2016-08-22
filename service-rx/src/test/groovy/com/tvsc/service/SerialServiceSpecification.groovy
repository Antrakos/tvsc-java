package com.tvsc.service

import com.tvsc.core.AppProfiles
import com.tvsc.core.model.Serial
import com.tvsc.service.config.ServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import rx.Observable
import rx.Single
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = ServiceConfig)
@ActiveProfiles(AppProfiles.TEST)
@Transactional
class SerialServiceSpecification extends Specification {
    @Autowired
    private SerialService serialService

    def "given future of serial info when get the info then check firstAired date and info doesn't contain seasons nad therefore episodes"() {
        given:
        Single<Serial> single = serialService.getSerialInfo(279121L)
        when:
        Serial serialInfo = single.toBlocking().value()
        then:
        serialInfo.firstAired == LocalDate.of(2014, 10, 7)
        serialInfo.seasons == null
    }

    def "given future of serial when get the object then check firstAired date and contains seasons and therefore episodes"() {
        given:
        Single<Serial> single = serialService.getSerial(279121L)
        when:
        Serial serial = single.toBlocking().value()
        then:
        serial.firstAired == LocalDate.of(2014, 10, 7)
        !serial.seasons.empty
    }

    def "given future of serial list when get the list then assure that all series has seasons and therefore episodes"() {
        given:
        Observable<Serial> observable = serialService.restoreAllData()
        when:
        List<Serial> series = observable.toList().toSingle().toBlocking().value()
        then:
        series.stream().allMatch { !it.seasons.empty }
    }

    def "when add serial then check that operation was successful"() {
        when:
        serialService.addSerial(279121L)
        then:
        serialService.count() - old(serialService.count()) == 1L
    }

    def "when delete serial then check that operation was successful"() {
        when:
        serialService.deleteSerial(78901L)
        then:
        old(serialService.count()) - serialService.count() == 1L
    }

}
