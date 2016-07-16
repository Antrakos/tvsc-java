package com.tvsc.service.utils.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tvsc.service.Constants
import com.tvsc.service.exception.JsonException
import com.tvsc.service.utils.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Taras Zubrei
 */
@Component
open class JsonUtilsImpl @Autowired constructor(val objectMapper: ObjectMapper) : JsonUtils {

    override fun getNext(json: String): Int? {
        val asText = JsonException("Cannot read tree").wrap { objectMapper.readTree(json) }.at("/links/next").asText()
        return if (asText == "null") null else Integer.valueOf(asText)
    }

    override fun getCount(json: String): Int {
        return JsonException("Cannot read tree").wrap { objectMapper.readTree(json) }.at("/links/last").asInt()
    }

    override fun <T> getListData(json: String, clazz: Class<T>): List<T> {
        return JsonException("Cannot read tree").wrap {
            objectMapper.readerFor(TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz)).readValue(objectMapper.readTree(json).at("/${Constants.ROOT}"))
        }
    }

    override fun <T> getSingleObject(json: String, clazz: Class<T>): T {
        return JsonException("Cannot read tree").wrap {
            objectMapper.readerFor(clazz).readValue(objectMapper.readTree(json).at("/${Constants.ROOT}"))
        }
    }
}
