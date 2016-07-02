package com.tvsc.service.utils

/**
 *
 * @author Taras Zubrei
 */
interface JsonUtils {
    fun getNext(json: String): Int?
    fun getCount(json: String): Int
    fun <T> getListData(json: String, clazz: Class<T>): List<T>
    fun <T> getSingleObject(json: String, clazz: Class<T>): T
}
