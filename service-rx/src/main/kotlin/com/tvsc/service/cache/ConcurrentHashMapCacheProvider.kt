package com.tvsc.service.cache

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @author Taras Zubrei
 */
@Component
@Primary
open class ConcurrentHashMapCacheProvider : CacheProvider {
    private val map: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    override fun get(key: String) = map[key]!!

    override fun put(key: String, value: String): String {
        map[key] = value
        return value
    }

    override fun hasKey(key: String) = map.containsKey(key)
}