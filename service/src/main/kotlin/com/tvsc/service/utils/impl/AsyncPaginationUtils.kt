package com.tvsc.service.utils.impl

import com.tvsc.service.utils.AsyncHttpUtils
import com.tvsc.service.utils.JsonUtils
import com.tvsc.service.utils.PaginationUtils
import org.asynchttpclient.netty.NettyResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
@Component
open class AsyncPaginationUtils @Autowired constructor(val jsonUtils: JsonUtils, val asyncHttpUtils: AsyncHttpUtils) : PaginationUtils {
    override fun <T> getFullResponse(url: String, clazz: Class<T>): List<T> {
        val response = asyncHttpUtils.get(url).get()
        val elements: List<T> = jsonUtils.getListData(response, clazz)
        val count = jsonUtils.getCount(response)
        if (count == 1)
            return elements

        val pages = Array(count - 1) { CompletableFuture<List<T>>() }
        val urlWithPage = url.plus(if (url.contains("?")) "&" else "?").plus("page=%d")
        (2..count).forEach {
            pages[it - 2] = asyncHttpUtils.get(String.format(urlWithPage, it)).thenApply {
                response ->
                return@thenApply jsonUtils.getListData(response, clazz)
            }
        }
        return pages.fold(elements) { res, future -> res.plus(future.get()) }
    }

}