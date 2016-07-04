package com.tvsc.service.utils.impl

import com.tvsc.core.exception.ExceptionUtil
import com.tvsc.service.exception.HttpException
import com.tvsc.service.utils.HttpUtils
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

/**
 * @author Taras Zubrei
 */
@Component
@Primary
open class HttpUtilsImpl @Autowired constructor(val httpClient: CloseableHttpClient) : HttpUtils {

    override fun get(url: String): String {
        val request = HttpGet(url)
        return ExceptionUtil.wrapException(HttpException(request.uri.path)) {
            (httpClient.execute(request) as CloseableHttpResponse).use {
                response ->
                return@wrapException EntityUtils.toString(response.entity)
            }
        }

    }
}
