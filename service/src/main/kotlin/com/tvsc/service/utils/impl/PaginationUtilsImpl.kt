package com.tvsc.service.utils.impl

import com.tvsc.service.utils.HttpUtils
import com.tvsc.service.utils.JsonUtils
import com.tvsc.service.utils.PaginationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 * @author Taras Zubrei
 */
@Component
open class PaginationUtilsImpl @Autowired constructor(val jsonUtils: JsonUtils, val httpUtils: HttpUtils) : PaginationUtils {

    override fun <T> getFullResponse(url: String, clazz: Class<T>): List<T> {
        var response = httpUtils.get(url)
        val elements = jsonUtils.getListData(response, clazz) as MutableList<T>

        var next = jsonUtils.getNext(response)
        while (next != null) {
            response = httpUtils.get(String.format(url + (if (url.contains("?")) "&" else "?") + "page=%d", next))
            next = jsonUtils.getNext(response)
            elements.addAll(jsonUtils.getListData(response, clazz))
        }
        return elements
    }
}