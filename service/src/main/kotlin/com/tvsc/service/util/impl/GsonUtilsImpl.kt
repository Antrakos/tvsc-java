package com.tvsc.service.util.impl

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tvsc.service.Constants
import com.tvsc.service.exception.JsonException
import com.tvsc.service.util.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 * @author Taras Zubrei
 */
@Component
open class GsonUtilsImpl @Autowired constructor(val gson: Gson) : JsonUtils {

    override fun getCount(json: String): Int = JsonException("Cannot read tree").wrap {
        gson.fromJson(json, JsonObject::class.java).get("links").asJsonObject.get("last").asInt
    }

    override fun <T> getListData(json: String, clazz: Class<T>): List<T> = JsonException("Cannot read tree").wrap {
        gson.fromJson(json, JsonObject::class.java).get(Constants.ROOT).asJsonArray.map { gson.fromJson(it, clazz) }
    }

    override fun <T> getSingleObject(json: String, clazz: Class<T>): T = JsonException("Cannot read tree").wrap {
        gson.fromJson(gson.fromJson(json, JsonObject::class.java).get(Constants.ROOT), clazz)
    }
}