package com.tvsc.service.performance

import com.tvsc.core.model.Episode
import com.tvsc.service.config.ServiceConfig
import com.tvsc.service.util.JsonUtils
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import java.util.concurrent.TimeUnit

/**
 *
 * @author Taras Zubrei
 */
@State(Scope.Benchmark)
@Warmup(iterations = 10)
@Measurement(iterations = 20)
@BenchmarkMode(Mode.All)
@Fork(value = 1, jvmArgs = "-server", warmups = 0, jvmArgsAppend = "-XX:+UseCompressedOops")
class JsonUtilsPerformanceTest {
    final static String template = """{"id": 4894849,"airedSeason": 2,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8}"""
    final static String jsonObject = """{"data": {"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8}}"""

    final static String jsonList = """{"data": [""" + (1..100).collect { String.format(template) }.join(',') + """]}"""

    final static def context = new AnnotationConfigApplicationContext(ServiceConfig)
    final static JsonUtils jsonUtils = context.getBean('jsonUtilsImpl') as JsonUtils
    final static JsonUtils gsonUtils = context.getBean('gsonUtilsImpl') as JsonUtils

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    void jacksonListData(Blackhole hole) {
        hole.consume(jsonUtils.getListData(jsonList, Episode))
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    void jacksonObjectData(Blackhole hole) {
        hole.consume(jsonUtils.getSingleObject(jsonObject, Episode))
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    void gsonListData(Blackhole hole) {
        hole.consume(gsonUtils.getListData(jsonList, Episode))
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    void gsonObjectData(Blackhole hole) {
        hole.consume(gsonUtils.getSingleObject(jsonObject, Episode))
    }
}
