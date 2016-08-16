package com.tvsc.service.util

/**
 *
 * @author Taras Zubrei
 */
interface JsonUtils {
    fun <T> getPage(json: String, clazz: Class<T>): Pair<List<T>, Int>
    fun <T> getListData(json: String, clazz: Class<T>): List<T>
    fun <T> getSingleObject(json: String, clazz: Class<T>): T
}
