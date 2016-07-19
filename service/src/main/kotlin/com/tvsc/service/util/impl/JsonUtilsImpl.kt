package com.tvsc.service.util.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tvsc.service.Constants
import com.tvsc.service.exception.JsonException
import com.tvsc.service.util.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

/**
 * @author Taras Zubrei
 */
@Component
@Primary
open class JsonUtilsImpl @Autowired constructor(val objectMapper: ObjectMapper) : JsonUtils {

    override fun getCount(json: String): Int = JsonException("Cannot read tree").wrap { objectMapper.readTree(json) }.at("/links/last").asInt()

    override fun <T> getListData(json: String, clazz: Class<T>): List<T> = JsonException("Cannot read tree").wrap {
        objectMapper.reader()
                .forType(TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz))
                .readValue(objectMapper.readTree(json).at("/data"))
    }

    override fun <T> getSingleObject(json: String, clazz: Class<T>): T = JsonException("Cannot read tree").wrap {
        objectMapper.reader().forType(clazz).withRootName(Constants.ROOT).readValue(json)
    }
}
