package com.tvsc.service.performance

import com.tvsc.core.model.Episode
import com.tvsc.core.model.Serial
import com.tvsc.service.Constants
import com.tvsc.service.config.ServiceConfig
import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import com.tvsc.service.util.impl.GsonUtilsImpl
import com.tvsc.service.util.impl.HttpUtilsImpl
import com.tvsc.service.util.impl.JsonUtilsImpl
import com.tvsc.service.util.impl.MoshiUtilsImpl
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

/**
 *
 * @author Taras Zubrei
 */
@State(Scope.Benchmark)
@Warmup(iterations = 10)
@Measurement(iterations = 20)
@Fork(value = 1, jvmArgs = "-server", warmups = 0, jvmArgsAppend = "-XX:+UseCompressedOops")
class JsonUtilsPerformanceTest {
    final static ServiceConfig config = new ServiceConfig()
    final static HttpUtils httpUtils = new HttpUtilsImpl(config.okHttpClient(), config.executorService())
    final static JsonUtils moshiUtils = new MoshiUtilsImpl(config.moshi())
    final static JsonUtils jsonUtils = new JsonUtilsImpl(config.objectMapper())
    final static JsonUtils gsonUtils = new GsonUtilsImpl(config.gson())

    @Benchmark
    void jacksonObjectData(Blackhole hole) {
        hole.consume(httpUtils.get(Constants.SERIES + 279121L).thenApply {
            jsonUtils.getSingleObject(it, Serial)
        }.join())
    }

    @Benchmark
    void moshiObjectData(Blackhole hole) {
        hole.consume(httpUtils.get(Constants.SERIES + 279121L).thenApply {
            moshiUtils.getSingleObject(it, Serial)
        }.join())
    }

    @Benchmark
    void gsonObjectData(Blackhole hole) {
        hole.consume(httpUtils.get(Constants.SERIES + 279121L).thenApply {
            gsonUtils.getSingleObject(it, Serial)
        }.join())
    }

    @Benchmark
    void jacksonListData(Blackhole hole) {
        hole.consume(httpUtils.get(Constants.SERIES + 279121L + "/episodes").thenApply {
            jsonUtils.getListData(it, Episode)
        }.join())
    }

    @Benchmark
    void moshiListData(Blackhole hole) {
        hole.consume(httpUtils.get(Constants.SERIES + 279121L + "/episodes").thenApply {
            moshiUtils.getListData(it, Episode)
        }.join())
    }

    @Benchmark
    void gsonListData(Blackhole hole) {
        hole.consume(httpUtils.get(Constants.SERIES + 279121L + "/episodes").thenApply {
            gsonUtils.getListData(it, Episode)
        }.join())
    }
}
