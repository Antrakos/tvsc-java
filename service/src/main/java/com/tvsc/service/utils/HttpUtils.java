package com.tvsc.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author Taras Zubrei
 */
@Component
public class HttpUtils {
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private JsonUtils jsonUtils;

    public <T> List<T> getFullResponse(String url, Class<T> clazz) throws IOException {
        HttpGet request = new HttpGet(url);
        String response = IOUtils.toString(httpClient.execute(request).getEntity().getContent(), "utf-8");
        request.releaseConnection();
        List<T> elements = jsonUtils.getListData(response, clazz);

        Integer next = jsonUtils.getNext(response);
        while (next != null) {
            request = new HttpGet(String.format(url + (url.contains("?") ? "&" : "?") + "page=%d", next));
            response = IOUtils.toString(httpClient.execute(request).getEntity().getContent(), "utf-8");
            request.releaseConnection();
            next = jsonUtils.getNext(response);
            elements.addAll(jsonUtils.getListData(response, clazz));
        }
        return elements;
    }
}
