package com.tvsc.service.utils;

import com.tvsc.service.exception.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
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

    public String get(String url) {
        final HttpGet request = new HttpGet(url);
        try (CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new HttpException(request.getURI().getPath());
        }
    }

    public <T> List<T> getFullResponse(String url, Class<T> clazz) {
        String response = get(url);
        List<T> elements = jsonUtils.getListData(response, clazz);

        Integer next = jsonUtils.getNext(response);
        while (next != null) {
            response = get(String.format(url + (url.contains("?") ? "&" : "?") + "page=%d", next));
            next = jsonUtils.getNext(response);
            elements.addAll(jsonUtils.getListData(response, clazz));
        }
        return elements;
    }
}
