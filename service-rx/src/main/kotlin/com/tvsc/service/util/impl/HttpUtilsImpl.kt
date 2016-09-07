package com.tvsc.service.util.impl

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.rx.rx_string
import com.tvsc.service.cache.CacheProvider
import com.tvsc.service.util.HttpUtils
import org.springframework.stereotype.Component
import rx.Single

/**
 *
 * @author Taras Zubrei
 */
@Component
open class HttpUtilsImpl constructor(val fuelManager: FuelManager,
                                     val cacheProvider: CacheProvider) : HttpUtils {
    override fun get(url: String): Single<String> =
            if (cacheProvider.hasKey(url))
                Single.just(cacheProvider.get(url))
            else
                fuelManager.request(Method.GET, url)
                        .rx_string()
                        .toSingle()
                        .map { cacheProvider.put(url, it) }
}