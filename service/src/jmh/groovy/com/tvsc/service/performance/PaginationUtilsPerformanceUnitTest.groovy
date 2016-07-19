package com.tvsc.service.performance

import com.tvsc.core.model.Episode
import com.tvsc.service.Constants
import com.tvsc.service.config.ServiceConfig
import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import com.tvsc.service.util.impl.JsonUtilsImpl
import com.tvsc.service.util.impl.PaginationUtilsImpl
import org.jetbrains.annotations.NotNull
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

import java.util.concurrent.CompletableFuture
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
class PaginationUtilsPerformanceUnitTest {
    private PaginationUtilsImpl paginationUtils
    private HttpUtils httpUtils
    private JsonUtils jsonUtils

    final static String template = """{"links": {"last": %d}, "data": [{"id": %d,"airedSeason": %d,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8}]}"""
    final static int pages = 5
    final static List<String> jsonPages = (1..pages).collect { String.format(template, pages, it * 100, it) }

    @Setup
    void setup() {
        ServiceConfig config = new ServiceConfig()
        jsonUtils = new JsonUtilsImpl(config.objectMapper())
        httpUtils = new HttpUtils() {
            @Override
            CompletableFuture<String> get(@NotNull String url) {
                CompletableFuture.supplyAsync {
                    url.find(/page=\d+/) ?: 'page=1'
                }.thenApply {
                    PaginationUtilsPerformanceUnitTest.jsonPages[it[5..-1].toInteger() - 1]
                }
            }
        }
        paginationUtils = new PaginationUtilsImpl(jsonUtils, httpUtils)
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    void newImplementation(Blackhole hole) {
        def completableFuture = paginationUtils.getFullResponse(Constants.SERIES + 279121L + "/episodes", Episode.class)
        def object = completableFuture.join()
        hole.consume(object)
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    void oldImplementation(Blackhole hole) {
        def completableFuture = paginationUtils.getFullResponseOldVersion(Constants.SERIES + 279121L + "/episodes", Episode.class)
        def object = completableFuture.join()
        hole.consume(object)
    }
}
