package com.tvsc.service.utils.impl

import com.tvsc.service.utils.AsyncHttpUtils
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
open class OkAsyncHttpUtils @Autowired constructor(val okHttpClient: OkHttpClient) : AsyncHttpUtils {
    override fun get(url: String): CompletableFuture<String> {
        val result = CompletableFuture<String>();
        okHttpClient.newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
            }

            override fun onResponse(call: Call?, response: Response?) {
                result.complete(response?.body()?.string())
            }

        })
        return result
    }
}