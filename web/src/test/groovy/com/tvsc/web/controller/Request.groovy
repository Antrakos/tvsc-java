package com.tvsc.web.controller

import groovy.transform.CompileStatic
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.util.ReflectionUtils

import java.lang.reflect.Method
import java.util.stream.Collectors

import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters

/**
 *
 * @author Taras Zubrei
 */
@CompileStatic
class Request {
    private MockHttpServletRequestBuilder method
    private ParameterDescriptor[] pathParameters
    private FieldDescriptor[] requestFields
    private FieldDescriptor[] responseFields
    private List<ResultMatcher> expectations

    Request(MockHttpServletRequestBuilder method, ParameterDescriptor[] pathParameters, FieldDescriptor[] requestFields, FieldDescriptor[] responseFields, List<ResultMatcher> expectations) {
        this.method = method
        this.pathParameters = pathParameters
        this.requestFields = requestFields
        this.responseFields = responseFields
        this.expectations = expectations
    }

    def perform(MockMvc mockMvc, RestDocumentationResultHandler document) {
        def actions = mockMvc.perform(method);
        if (pathParameters.length > 0)
            document.snippets(pathParameters(pathParameters));
        if (responseFields.length > 0)
            document.snippets(responseFields(responseFields));
        if (requestFields.length > 0)
            document.snippets(requestFields(requestFields));
        expectations.each { actions.andExpect(it) }
        actions.andDo(document)
        actions.andReturn()
    }

    @CompileStatic
    static class Builder {
        private Properties properties = new Properties()
        private MockHttpServletRequestBuilder requestBuilder
        private ParameterDescriptor[] pathParameters
        private FieldDescriptor[] requestFields
        private FieldDescriptor[] responseFields
        private List<ResultMatcher> expectations = []

        Builder(RequestMethod method, String url, Object... pathValues) {
            properties.load(new ClassPathResource('web-test.properties').inputStream)
            Object[] var = []
            final Method httpMethod = ReflectionUtils.findMethod(RestDocumentationRequestBuilders, method.toString().toLowerCase(), String, var.class);
            requestBuilder = (httpMethod.invoke(this, url, pathValues) as MockHttpServletRequestBuilder)
                    .contentType(MediaType.APPLICATION_JSON)
        }

        def addHeader(String key, String value) {
            requestBuilder = requestBuilder.header(key, value)
            this
        }

        def withContent(String content) {
            requestBuilder.content(content)
            this
        }

        def andExpect(ResultMatcher matcher) {
            expectations.add(matcher)
            this
        }

        def withResponseBodyFromDto(Class<?> dto, String... exclusions) {
            responseFields = getFields(dto, exclusions)
            this
        }

        def withRequestBodyFromDto(Class<?> dto, String... exclusions) {
            requestFields = getFields(dto, exclusions)
            this
        }

        def withPathParametersFromMethod(Method method) {
            //TODO: get description from method metadata and NOT NULL
//            pathParameters = Arrays.stream(method.parameters)
//                    .filter { it.getDeclaredAnnotationsByType(PathVariable.class) != null }
////                    .peek { it.name .initParameterNameDiscovery(parameterNameDiscoverer) }
//                    .map { new Pair<String, String>(it.name, 'THE DESCRIPTION FROM PROPERTIES') }
//                    .map { parameterWithName(it.first).description(it.second) }
//                    .toArray(ParameterDescriptor[]:: new) //TODO: Groovy method reference
            this //TODO: Maybe Kotlin?
        }

        def build() {
            new Request(requestBuilder, pathParameters, requestFields, responseFields, expectations)
        }

        private FieldDescriptor[] getFields(Class<?> dto, String... exclusions) {
            dto.getDeclaredFields().size() != 0 ? Arrays.stream(dto.getDeclaredFields())
                    .map { it.name }
                    .filter { !it.contains('$') }
                    .filter { it != 'metaclass' }
                    .filter { !exclusions.contains(it) }
                    .map { "${dto.simpleName}.$it" }
                    .map { fieldWithPath(it).description(properties.getProperty(it)) }
                    .collect(Collectors.toList())
                    .toArray(new FieldDescriptor[0]) : new FieldDescriptor[0]
        }

    }

    enum RequestMethod {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE;

        private RequestMethod() {}
    }


}
