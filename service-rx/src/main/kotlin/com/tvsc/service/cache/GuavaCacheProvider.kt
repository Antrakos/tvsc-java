package com.tvsc.service.cache

import com.google.common.cache.CacheBuilder
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 *
 * @author Taras Zubrei
 */
@Component
open class GuavaCacheProvider : CacheProvider {
    private val map = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build<String, String>()

    override fun get(key: String) = map.getIfPresent(key)!!

    override fun put(key: String, value: String): String {
        map.put(key, value)
        return value
    }

    override fun hasKey(key: String) = map.asMap().containsKey(key)
}