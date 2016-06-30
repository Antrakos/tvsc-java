package com.tvsc.service.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tvsc.core.exception.ExceptionUtil
import com.tvsc.service.Constants
import com.tvsc.service.exception.JsonException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Taras Zubrei
 */
@Component
open class JsonUtils {
    @Autowired
    private val objectMapper: ObjectMapper? = null

    fun getNext(json: String): Int? {
        val asText = ExceptionUtil.wrapCheckedException(JsonException("Cannot read tree")) {
            objectMapper!!.readTree(json)
        }.at("/links/next").asText()
        return if (asText == "null") null else Integer.valueOf(asText)
    }

    fun <T> getListData(json: String, clazz: Class<T>): List<T> {
        return ExceptionUtil.wrapCheckedException(JsonException("Cannot read tree")) {
            objectMapper!!.reader().forType(TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz)).readValue<List<T>>(objectMapper.readTree(json).at("/data"))
        }
    }

    fun <T> getSingleObject(json: String, clazz: Class<T>): T {
        return ExceptionUtil.wrapCheckedException(JsonException("Cannot read tree")) {
            objectMapper!!.reader().forType(clazz).withRootName(Constants.ROOT).readValue<T>(json)
        }
    }
}
