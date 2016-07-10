package com.tvsc.service.util.impl

import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import com.tvsc.service.util.PaginationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
@Component
open class PaginationUtilsImpl @Autowired constructor(val jsonUtils: JsonUtils, val httpUtils: HttpUtils) : PaginationUtils {
    override fun <T> getFullResponse(url: String, clazz: Class<T>): CompletableFuture<List<T>> {
        return httpUtils.get(url).thenApply {
            response ->
            val elements: List<T> = jsonUtils.getListData(response, clazz)
            val count = jsonUtils.getCount(response)
            if (count == 1)
                return@thenApply elements
            val pages = Array(count - 1) { CompletableFuture<List<T>>() }
            val urlWithPage = url.plus(if (url.contains("?")) "&" else "?").plus("page=%d")
            (2..count).forEach {
                pages[it - 2] = getPage(urlWithPage, it, clazz)
            }
            return@thenApply pages.fold(elements) { res, future -> res.plus(future.get()) }
        }
    }

    private fun <T> getPage(url: String, page: Int, clazz: Class<T>): CompletableFuture<List<T>> =
            httpUtils.get(String.Companion.format(url, page)).thenApply {
                response ->
                return@thenApply jsonUtils.getListData(response, clazz)
            }

}