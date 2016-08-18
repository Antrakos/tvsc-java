package com.tvsc.service.cache

/**
 *
 * @author Taras Zubrei
 */
interface CacheProvider {
    fun get(key: String): String
    fun put(key: String, value: String): String
    fun hasKey(key: String): Boolean
}