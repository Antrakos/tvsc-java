package com.tvsc.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.tvsc.core.AppProfiles
import com.tvsc.service.util.JsonUtils
import com.tvsc.web.Routes
import com.tvsc.web.SerialDto
import com.tvsc.web.config.WebMvcConfig
import groovy.util.logging.Slf4j
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = WebMvcConfig)
@ActiveProfiles(AppProfiles.TEST)
@WebAppConfiguration
@Slf4j
class DocsGenerationTest extends Specification {
    @Rule
    JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets")
    @Autowired
    private WebApplicationContext context
    @Autowired
    private ObjectMapper objectMapper
    @Autowired
    private JsonUtils jsonUtils

    def setup() {
        Request.metaClass.perform = {
            delegate.perform(MockMvcBuilders.webAppContextSetup(this.context)
                    .apply(documentationConfiguration(this.restDocumentation))
                    .build(), objectMapper)
        }
    }

    def "get all series"() {
        given:
        Request request = new Request.Builder(Request.RequestMethod.GET, Routes.SERIES)
                .withResponseBodyFromDto(SerialDto, Request.Type.ARRAY)
                .andExpect(status().isOk())
                .build()
        when:
        def response = request.perform().response.contentAsString
        then:
        !response.contains('"null"')
    }

}
