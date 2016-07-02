package com.tvsc.service.utils

/**
 *
 * @author Taras Zubrei
 */
interface PaginationUtils {
    fun <T> getFullResponse(url: String, clazz: Class<T>): List<T>
}
