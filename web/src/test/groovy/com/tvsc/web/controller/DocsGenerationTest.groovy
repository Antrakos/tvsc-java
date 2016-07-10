package com.tvsc.web.controller

import com.tvsc.core.model.Serial
import com.tvsc.web.EpisodeDto
import com.tvsc.web.Routes
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 *
 * @author Taras Zubrei
 */
//@ContextConfiguration('classpath*:WebMvcConfig.groovy')
//@ActiveProfiles(AppProfiles.TEST)
//@WebAppConfiguration
//@Slf4j
class DocsGenerationTest extends Specification {
//    @Rule
//    def restDocumentation = new JUnitRestDocumentation("build/generated-snippets")
//    @Autowired
//    private WebApplicationContext context
//    @Autowired
//    private ObjectMapper objectMapper
//    @Autowired
//    private JsonUtils jsonUtils
//
//    def setup() {
//        Request.metaClass.perform = {
//            delegate.perform(MockMvcBuilders.webAppContextSetup(this.context)
//                    .apply(documentationConfiguration(this.restDocumentation))
//                    .build(), objectMapper)
//        }
//    }

    def "get all series"() {
        when:
        Request request = new Request.Builder(Request.RequestMethod.GET, Routes.SERIES)
                .withResponseBodyFromDto(Serial, Request.Type.ARRAY)
                .andExpect(status().isOk())
                .build()
        when:
        def response = jsonUtils.getListData(request.perform().response.contentAsString, EpisodeDto)
        then:
        response.size() > 0
    }

}
