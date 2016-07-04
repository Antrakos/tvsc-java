package com.tvsc.service.utils.impl

import com.tvsc.service.utils.HttpUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 * @author Taras Zubrei
 */
@Component
open class OkHttpUtils @Autowired constructor(val okHttpClient: OkHttpClient) : HttpUtils{
    override fun get(url: String): String = okHttpClient.newCall(Request.Builder().url(url).build()).execute().body().string()
}