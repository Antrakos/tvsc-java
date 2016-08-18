package com.tvsc.service.util.impl

import com.tvsc.service.util.HttpUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.Observable
import rx.Single
import java.io.IOException

/**
 *
 * @author Taras Zubrei
 */
@Component
open class HttpUtilsImpl @Autowired constructor(val okHttpClient: OkHttpClient) : HttpUtils {
    override fun get(url: String): Single<String> = getResponse(url).map { it.body().string() }
    private fun getResponse(url: String) = Observable.defer {
        try {
            val response = okHttpClient.newCall(Request.Builder().url(url).build()).execute()
            Observable.just(response)
        } catch (e: IOException) {
            Observable.error<Response>(e)
        }
    }.toSingle()
}