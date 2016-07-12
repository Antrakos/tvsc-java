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
        val urlWithPage = url.plus(if (url.contains('?')) '&' else '?').plus("page=%d")
        return httpUtils.get(url).thenApply { jsonUtils.getListData(it, clazz).to(jsonUtils.getCount(it)) }
                .thenApply { pair ->
                    Array<CompletableFuture<List<T>>>(pair.second) {
                        if (it == 0) CompletableFuture.completedFuture(pair.first) else getPage(urlWithPage, it + 1, clazz)
                    }
                }
                .thenApply { it.reduce { res, completableFuture -> res.thenCombine(completableFuture) { all, current -> all.plus(current) } } }
                .thenApply { it.join() }
    }

    private fun <T> getPage(url: String, page: Int, clazz: Class<T>): CompletableFuture<List<T>> =
            httpUtils.get(String.Companion.format(url, page)).thenApply { jsonUtils.getListData(it, clazz) }

    @Deprecated("This method is too slow. Use 'getFullResponse'.", ReplaceWith("getFullResponse(url, clazz)"), DeprecationLevel.WARNING)
    fun <T> getFullResponseOldVersion(url: String, clazz: Class<T>): CompletableFuture<List<T>> =
            httpUtils.get(url).thenApply {
                response ->
                val elements: List<T> = jsonUtils.getListData(response, clazz)
                val count = jsonUtils.getCount(response)
                if (count == 1)
                    return@thenApply elements
                val urlWithPage = url.plus(if (url.contains('?')) '&' else '?').plus("page=%d")
                return@thenApply (2..count).asSequence().map { getPage(urlWithPage, it, clazz) }.fold(elements) { res, future -> res.plus(future.get()) }
            }
}
