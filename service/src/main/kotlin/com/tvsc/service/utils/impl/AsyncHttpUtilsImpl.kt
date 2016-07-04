package com.tvsc.service.utils.impl

import com.tvsc.service.utils.AsyncHttpUtils
import org.asynchttpclient.AsyncHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
@Component
@Primary
open class AsyncHttpUtilsImpl @Autowired constructor(val httpAsyncClient: AsyncHttpClient) : AsyncHttpUtils {
    override fun get(url: String): CompletableFuture<String> {
        return httpAsyncClient.prepareGet(url).execute().toCompletableFuture().thenApply {
            return@thenApply it.responseBody
        }
    }
}