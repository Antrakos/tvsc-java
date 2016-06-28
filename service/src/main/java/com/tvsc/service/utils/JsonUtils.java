package com.tvsc.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tvsc.core.exception.ExceptionUtil;
import com.tvsc.service.Constants;
import com.tvsc.service.exception.JsonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Component
public class JsonUtils {
    @Autowired
    private ObjectMapper objectMapper;

    public Integer getNext(String json) {
        String asText = ExceptionUtil.wrapCheckedException(() -> objectMapper.readTree(json), new JsonException("Cannot read tree"))
                .at("/links/next").asText();
        return asText.equals("null") ? null : Integer.valueOf(asText);
    }

    public <T> List<T> getListData(String json, Class<T> clazz) {
        return ExceptionUtil.<List<T>>wrapCheckedException(() -> objectMapper
                        .reader()
                        .forType(TypeFactory.defaultInstance().constructCollectionType(List.class, clazz))
                        .readValue(objectMapper.readTree(json).at("/data")),
                new JsonException("Cannot read tree"));
    }

    public <T> T getSingleObject(String json, Class<T> clazz) {
        return ExceptionUtil.<T>wrapCheckedException(() -> objectMapper
                        .reader()
                        .forType(clazz)
                        .withRootName(Constants.ROOT)
                        .readValue(json),
                new JsonException("Cannot read tree"));
    }
}
