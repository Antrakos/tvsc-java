package com.tvsc.service.util

import okio.BufferedSource
import java.io.InputStream
import java.io.Reader
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
interface HttpUtils {
    fun get(url: String): CompletableFuture<String>
}