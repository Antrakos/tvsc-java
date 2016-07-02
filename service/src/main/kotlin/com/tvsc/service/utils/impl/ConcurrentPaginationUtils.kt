package com.tvsc.service.utils.impl

import com.tvsc.service.utils.HttpUtils
import com.tvsc.service.utils.JsonUtils
import com.tvsc.service.utils.PaginationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier

/**
 *
 * @author Taras Zubrei
 */
@Component
@Primary
open class ConcurrentPaginationUtils @Autowired constructor(val jsonUtils: JsonUtils, val httpUtils: HttpUtils) : PaginationUtils {
    override fun <T> getFullResponse(url: String, clazz: Class<T>): List<T> {
        var response = httpUtils.get(url)
        val elements: List<T> = jsonUtils.getListData(response, clazz)

        val count = jsonUtils.getCount(response)
        if (count == 1)
            return elements

        val pages = Array(count - 1) { CompletableFuture<List<T>>() }
        val executor = Executors.newFixedThreadPool(count - 1)
        val urlWithPage = url.plus(if (url.contains("?")) "&" else "?").plus("page=%d")
        (2..count).forEach {
            pages[it - 2] = CompletableFuture.supplyAsync(Supplier { //TODO: ForkJoinPool.commonPool() or should we use own ExecutorService?
                response = httpUtils.get(String.format(urlWithPage, it))
                return@Supplier jsonUtils.getListData(response, clazz)
            }, executor)
        }
        executor.shutdown()
        return pages.fold(elements) { res, future -> res.plus(future.get()) }
    }
}
