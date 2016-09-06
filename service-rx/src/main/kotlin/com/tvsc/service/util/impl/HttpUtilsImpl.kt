package com.tvsc.service.util.impl

import com.tvsc.service.cache.CacheProvider
import com.tvsc.service.util.HttpUtils
import org.asynchttpclient.AsyncHttpClient
import org.asynchttpclient.BoundRequestBuilder
import org.asynchttpclient.RequestBuilder
import org.asynchttpclient.Response
import org.asynchttpclient.extras.rxjava.single.AsyncHttpSingle
import org.springframework.stereotype.Component
import rx.Single

/**
 *
 * @author Taras Zubrei
 */
@Component
open class HttpUtilsImpl constructor(val asyncHttpClient: AsyncHttpClient,
                                     val cacheProvider: CacheProvider) : HttpUtils {
    override fun get(url: String): Single<String> =
            if (cacheProvider.hasKey(url))
                Single.just(cacheProvider.get(url))
            else
                getResponse(url)
                        .map { it.responseBody }
                        .map { cacheProvider.put(url, it) }


    private fun getResponse(url: String): Single<Response> = AsyncHttpSingle.create(
            BoundRequestBuilder(asyncHttpClient, RequestBuilder("GET").setUrl(url).build())
    )
}