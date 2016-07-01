package com.tvsc.service.utils

import com.tvsc.service.exception.HttpException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.io.IOException

/**
 * @author Taras Zubrei
 */
@Component
open class HttpUtils @Autowired constructor(val httpClient: HttpClient) {

    fun get(url: String): String {
        val request = HttpGet(url)
        try {
            (httpClient.execute(request) as CloseableHttpResponse).use {
                response ->
                return EntityUtils.toString(response.entity)
            }
        } catch (e: IOException) {
            throw HttpException(request.uri.path)
        }

    }
}
