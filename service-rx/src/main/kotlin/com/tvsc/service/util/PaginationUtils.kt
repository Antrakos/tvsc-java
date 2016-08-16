package com.tvsc.service.util

import rx.Observable

/**
 *
 * @author Taras Zubrei
 */
interface PaginationUtils {
    fun <T> getFullResponse(url: String, clazz: Class<T>): Observable<T>
}
