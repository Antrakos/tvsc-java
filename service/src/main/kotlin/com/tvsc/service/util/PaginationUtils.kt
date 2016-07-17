package com.tvsc.service.util

import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
interface PaginationUtils {
    fun <T> getFullResponse(url: String, clazz: Class<T>): CompletableFuture<List<T>>
}
