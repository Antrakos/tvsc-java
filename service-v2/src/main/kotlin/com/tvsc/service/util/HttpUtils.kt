package com.tvsc.service.util

import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
interface HttpUtils {
    fun get(url: String): CompletableFuture<String>
}