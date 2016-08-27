package com.tvsc.service.util.impl

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType
import com.tvsc.service.json.Page
import com.tvsc.service.util.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.reflect.Type

/**
 *
 * @author Taras Zubrei
 */
@Component
open class MoshiUtilsImpl @Autowired constructor(val moshi: Moshi) : JsonUtils {
    override fun <T> getPage(json: String, clazz: Class<T>): Pair<List<T>, Int> =
            moshi.adapter<Page<List<T>>>(unwrap(list(clazz))).fromJson(json).let { it.data!!.to(it.links!!.last) }

    override fun <T> getListData(json: String, clazz: Class<T>): List<T> =
            moshi.adapter<Page<List<T>>>(unwrap(list(clazz))).fromJson(json).data!!

    override fun <T> getSingleObject(json: String, clazz: Class<T>): T =
            moshi.adapter<Page<T>>(unwrap(clazz)).fromJson(json).data!!

    private fun unwrap(type: Type) = newParameterizedType(Page::class.java, type)
    private fun list(type: Type) = newParameterizedType(List::class.java, type)
}