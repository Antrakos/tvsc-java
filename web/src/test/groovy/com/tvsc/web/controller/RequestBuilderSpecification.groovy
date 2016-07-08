package com.tvsc.web.controller

import com.tvsc.web.EpisodeDto
import spock.lang.Specification

/**
 *
 * @author Taras Zubrei
 */
class RequestBuilderSpecification extends Specification {
    def "check"() {
        when:
        def builder = new Request.Builder(Request.RequestMethod.GET, '')
                .withResponseBodyFromDto(EpisodeDto)
                .build()
        then:
        builder != null
    }
}
