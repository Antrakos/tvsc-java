package com.tvsc.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author Taras Zubrei
 */
@Component
public class JsonUtils {
    @Autowired
    private ObjectMapper objectMapper;

    public Integer getNext(String json) throws IOException {
        String asText = objectMapper.readTree(json)
                .at("/links/next").asText();
        return asText.equals("null") ? null : Integer.valueOf(asText);
    }

    public <T> List<T> getListData(String json, Class<T> clazz) throws IOException {
        return objectMapper
                .reader()
                .forType(TypeFactory.defaultInstance().constructCollectionType(List.class, clazz))
                .readValue(objectMapper
                        .readTree(json)
                        .at("/data"));
    }
}
