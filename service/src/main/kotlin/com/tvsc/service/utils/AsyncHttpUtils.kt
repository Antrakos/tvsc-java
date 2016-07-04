package com.tvsc.service.utils

import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
interface AsyncHttpUtils {
    fun get(url: String): CompletableFuture<String>
}