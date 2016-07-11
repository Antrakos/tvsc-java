package com.tvsc.service.util.impl

import com.tvsc.core.exception.ExceptionUtil
import com.tvsc.service.exception.HttpException
import com.tvsc.service.exception.NotFoundException
import com.tvsc.service.util.HttpUtils
import okhttp3.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
@Component
open class HttpUtilsImpl @Autowired constructor(val okHttpClient: OkHttpClient) : HttpUtils {
    override fun get(url: String): CompletableFuture<String> {
        val result = CompletableFuture<String>();
        okHttpClient.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                if (e != null)
                    ExceptionUtil.wrapException(HttpException(url)) { throw e }
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response?.code() == 404)
                    throw NotFoundException(url)
                result.complete(response?.body()?.string())
            }
        })
        return result
    }
}