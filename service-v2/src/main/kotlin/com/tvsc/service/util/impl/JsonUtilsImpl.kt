package com.tvsc.service.util.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tvsc.core.exception.ExceptionUtil
import com.tvsc.service.Constants
import com.tvsc.service.exception.JsonException
import com.tvsc.service.util.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Taras Zubrei
 */
@Component
open class JsonUtilsImpl @Autowired constructor(val objectMapper: ObjectMapper) : JsonUtils {

    override fun getCount(json: String): Int = ExceptionUtil.wrapException(JsonException("Cannot read tree")) { objectMapper.readTree(json) }.at("/links/last").asInt()

    override fun <T> getListData(json: String, clazz: Class<T>): List<T> = ExceptionUtil.wrapException(JsonException("Cannot read tree")) {
        objectMapper.reader()
                .forType(TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz))
                .readValue(objectMapper.readTree(json).at("/data"))
    }

    override fun <T> getSingleObject(json: String, clazz: Class<T>): T = ExceptionUtil.wrapException(JsonException("Cannot read tree")) {
        objectMapper.reader().forType(clazz).withRootName(Constants.ROOT).readValue(json)
    }
}
