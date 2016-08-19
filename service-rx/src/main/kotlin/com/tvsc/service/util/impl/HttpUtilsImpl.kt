package com.tvsc.service.util.impl

import com.tvsc.service.cache.CacheProvider
import com.tvsc.service.util.HttpUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.Observable
import rx.Single

/**
 *
 * @author Taras Zubrei
 */
@Component
open class HttpUtilsImpl @Autowired constructor(val okHttpClient: OkHttpClient,
                                                val cacheProvider: CacheProvider) : HttpUtils {
    override fun get(url: String): Single<String> =
            if (cacheProvider.hasKey(url))
                Single.just(cacheProvider.get(url))
            else
                getResponse(url)
                        .map { it.body().string() }
                        .map { cacheProvider.put(url, it) }


    private fun getResponse(url: String) = Observable.defer {
        try {
            val response = okHttpClient.newCall(Request.Builder().url(url).build()).execute()
            Observable.just(response)
        } catch (e: Throwable) {
            Observable.error<Response>(e)
        }
    }.toSingle()
}