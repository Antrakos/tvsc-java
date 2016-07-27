package com.tvsc.service.util.impl

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.tvsc.service.Constants
import com.tvsc.service.exception.JsonException
import com.tvsc.service.json.Page
import com.tvsc.service.util.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.io.Reader

/**
 * @author Taras Zubrei
 */
@Component
@Primary
open class JsonUtilsImpl @Autowired constructor(val objectMapper: ObjectMapper) : JsonUtils {

    override fun <T> getPage(json: Reader, clazz: Class<T>): Pair<List<T>, Int> = JsonException("Cannot read tree").wrap {
        objectMapper.readerFor(unwrap(list(clazz))).readValue<Page<List<T>>>(json)
    }.let { it.data!!.to(it.links!!.last) }

    override fun <T> getListData(json: Reader, clazz: Class<T>): List<T> = JsonException("Cannot read tree").wrap {
        objectMapper.reader()
                .forType(list(clazz))
                .readValue(objectMapper.readTree(json).at("/data"))
    }

    override fun <T> getSingleObject(json: Reader, clazz: Class<T>): T = JsonException("Cannot read tree").wrap {
        objectMapper.readerFor(clazz).withRootName(Constants.ROOT).readValue(json);
    }

    private fun list(clazz: Class<*>) = TypeFactory.defaultInstance().constructCollectionType(List::class.java, clazz)
    private fun unwrap(clazz: JavaType) = TypeFactory.defaultInstance().constructParametricType(Page::class.java, clazz)
}
