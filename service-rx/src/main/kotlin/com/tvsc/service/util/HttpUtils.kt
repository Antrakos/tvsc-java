package com.tvsc.service.util

import rx.Single

/**
 *
 * @author Taras Zubrei
 */
interface HttpUtils {
    fun get(url: String): Single<String>
}