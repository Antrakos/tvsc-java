package com.tvsc.service.util

import java.io.Reader

/**
 *
 * @author Taras Zubrei
 */
interface JsonUtils {
    fun <T> getPage(json: Reader, clazz: Class<T>): Pair<List<T>, Int>
    fun <T> getListData(json: Reader, clazz: Class<T>): List<T>
    fun <T> getSingleObject(json: Reader, clazz: Class<T>): T
}
