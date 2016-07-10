package com.tvsc.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.util.ReflectionUtils

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.stream.Collectors

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
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

    def getUrl() {
        def url = method.class.getDeclaredField('url')
        url.accessible = true
        (ReflectionUtils.getField(url, method) as URI).path;
    }

    def perform(MockMvc mockMvc, ObjectMapper objectMapper) {
        def actions = mockMvc.perform(method);
        expectations.each { actions.andExpect(it) }
        actions.andDo(document('',
                preprocessRequest(prettyPrint()),
                preprocessResponse(limit(3, objectMapper), prettyPrint()),
                pathParameters(pathParameters),
                responseFields(responseFields),
                requestFields(requestFields)))
        actions.andReturn()
    }

    private static OperationPreprocessor limit(int limit, ObjectMapper objectMapper) {
        return new ContentModifyingOperationPreprocessor({ byte[] originalContent, contentType ->
            if (originalContent.length > 0 && "[" == new String(originalContent, 0, 1)) {
                objectMapper.writeValueAsBytes(objectMapper.readValue(originalContent, List.class)
                        .stream().limit(limit).collect(Collectors.toList()));
            } else
                originalContent;
        });
    }

    @CompileStatic
    static class Builder {
        private final Properties properties = new Properties()
        private final MockHttpServletRequestBuilder requestBuilder
        private ParameterDescriptor[] pathParameters
        private FieldDescriptor[] requestFields
        private FieldDescriptor[] responseFields
        private final List<ResultMatcher> expectations = []

        Builder(RequestMethod method, String url, Object... pathValues) {
            properties.load(new ClassPathResource('web-test.properties').inputStream)
            Object[] var = []
            final Method httpMethod = ReflectionUtils.findMethod(RestDocumentationRequestBuilders, method.toString().toLowerCase(), String, var.class);
            requestBuilder = (httpMethod.invoke(this, url, pathValues) as MockHttpServletRequestBuilder)
                    .contentType(MediaType.APPLICATION_JSON)
        }

        def addHeader(String key, String value) {
            requestBuilder.header(key, value)
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

        def withResponseBodyFromDto(Class<?> dto, Type type, String... exclusions) {
            responseFields = getFields(dto, type, exclusions)
            this
        }

        def withRequestBodyFromDto(Class<?> dto, Type type, String... exclusions) {
            requestFields = getFields(dto, type, exclusions)
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

        private FieldDescriptor[] getFields(Class<?> dto, Type type, String... exclusions) {
            dto.getDeclaredFields().size() != 0 ? Arrays.stream(dto.getDeclaredFields())
                    .map { it.name }
                    .filter { !it.contains('$') }
                    .filter { it != 'metaclass' }
                    .filter { !exclusions.contains(it) }
                    .map { "${dto.simpleName}.$it" }
                    .map { fieldWithPath(type == Type.ARRAY ? "[].$it" : it).description(properties.getProperty(it)) }
                    .collect(Collectors.toList())
                    .toArray(new FieldDescriptor[0]) : new FieldDescriptor[0]
        }

    }

    enum Type {
        ARRAY,
        OBJECT;
        private Type() {}
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
