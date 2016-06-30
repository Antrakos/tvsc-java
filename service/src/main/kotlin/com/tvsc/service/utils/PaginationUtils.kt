package com.tvsc.service.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 * @author Taras Zubrei
 */
@Component
open class PaginationUtils {
    @Autowired
    private val jsonUtils: JsonUtils? = null
    @Autowired
    private val httpUtils: HttpUtils? = null

    fun <T> getFullResponse(url: String, clazz: Class<T>): List<T> {
        var response = httpUtils!!.get(url)
        val elements: List<T> = jsonUtils!!.getListData(response, clazz)

        var next = jsonUtils.getNext(response)
        while (next != null) {
            response = httpUtils.get(String.format(url + (if (url.contains("?")) "&" else "?") + "page=%d", next))
            next = jsonUtils.getNext(response)
            elements.plus(jsonUtils.getListData(response, clazz))
        }
        return elements
    }
}