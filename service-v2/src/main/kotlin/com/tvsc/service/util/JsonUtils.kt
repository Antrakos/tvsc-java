package com.tvsc.service.util

/**
 *
 * @author Taras Zubrei
 */
interface JsonUtils {
    fun getCount(json: String): Int
    fun <T> getListData(json: String, clazz: Class<T>): List<T>
    fun <T> getSingleObject(json: String, clazz: Class<T>): T
}
