package com.tvsc.service.utils

import com.tvsc.core.AppProfiles
import com.tvsc.core.model.Episode
import com.tvsc.service.Constants
import com.tvsc.service.config.ServiceConfig
import com.tvsc.service.utils.impl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Ignore
import spock.lang.Specification

/**
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = ServiceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
@Ignore
class PaginationUtilsComparisonPerformanceTest extends Specification {
    @Autowired
    private PaginationUtilsImpl paginationUtils
    @Autowired
    private ConcurrentPaginationUtils concurrentPaginationUtils
    @Autowired
    private AsyncPaginationUtils asyncPaginationUtils
    @Autowired
    private JsonUtils jsonUtils
    @Autowired
    private OkHttpUtils okHttpUtils
    @Autowired
    private OkAsyncHttpUtils okAsyncHttpUtils

    private PaginationUtilsImpl okPaginationUtils
    private AsyncPaginationUtils okAsyncPaginationUtils

    private final static SERIAL_ID_FOR_4_PAGE_DATA = 72108L
    private final static EPISODES_COUNT_FOR_4_PAGE_DATA = 309

    private final static SERIAL_ID_FOR_3_PAGE_DATA = 78901L
    private final static EPISODES_COUNT_FOR_3_PAGE_DATA = 245

    private final static SERIAL_ID_FOR_2_PAGE_DATA = 248736L
    private final static EPISODES_COUNT_FOR_2_PAGE_DATA = 123

    private final static SERIAL_ID_FOR_1_PAGE_DATA = 284066L
    private final static EPISODES_COUNT_FOR_1_PAGE_DATA = 11

    private final static REPEAT = 10

    def setupSpec() {
        PaginationUtils.metaClass.getById = {
            id -> delegate.getFullResponse("${Constants.SERIES}$id/episodes", Episode.class)
        }
        PaginationUtils.metaClass.methods[0] = null
    }

    def setup() {
        okPaginationUtils = new PaginationUtilsImpl(jsonUtils, okHttpUtils)
        okAsyncPaginationUtils = new AsyncPaginationUtils(jsonUtils, okAsyncHttpUtils)
    }

    /*Concurrent implementation*/

    def "concurrent util for 4-page data"() {
        when:
        List<Episode> episodesOfSerial = concurrentPaginationUtils.getById(SERIAL_ID_FOR_4_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_4_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "concurrent util for 3-page data"() {
        when:
        List<Episode> episodesOfSerial = concurrentPaginationUtils.getById(SERIAL_ID_FOR_3_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_3_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "concurrent util for 2-page data"() {
        when:
        List<Episode> episodesOfSerial = concurrentPaginationUtils.getById(SERIAL_ID_FOR_2_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_2_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "concurrent util for 1-page data"() {
        when:
        List<Episode> episodesOfSerial = concurrentPaginationUtils.getById(SERIAL_ID_FOR_1_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_1_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    /*Standard implementation*/

    def "standard util for 4-page data"() {
        when:
        List<Episode> episodesOfSerial = paginationUtils.getById(SERIAL_ID_FOR_4_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_4_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "standard util for 3-page data"() {
        when:
        List<Episode> episodesOfSerial = paginationUtils.getById(SERIAL_ID_FOR_3_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_3_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "standard util for 2-page data"() {
        when:
        List<Episode> episodesOfSerial = paginationUtils.getById(SERIAL_ID_FOR_2_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_2_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "standard util for 1-page data"() {
        when:
        List<Episode> episodesOfSerial = paginationUtils.getById(SERIAL_ID_FOR_1_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_1_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    /*Asynchronous implementation*/

    def "async util for 4-page data"() {
        when:
        List<Episode> episodesOfSerial = asyncPaginationUtils.getById(SERIAL_ID_FOR_4_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_4_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "async util for 3-page data"() {
        when:
        List<Episode> episodesOfSerial = asyncPaginationUtils.getById(SERIAL_ID_FOR_3_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_3_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "async util for 2-page data"() {
        when:
        List<Episode> episodesOfSerial = asyncPaginationUtils.getById(SERIAL_ID_FOR_2_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_2_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "async util for 1-page data"() {
        when:
        List<Episode> episodesOfSerial = asyncPaginationUtils.getById(SERIAL_ID_FOR_1_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_1_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    /*Asynchronous implementation by OkHTTP library*/

    def "ok async util for 4-page data"() {
        when:
        List<Episode> episodesOfSerial = okAsyncPaginationUtils.getById(SERIAL_ID_FOR_4_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_4_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "ok async util for 3-page data"() {
        when:
        List<Episode> episodesOfSerial = okAsyncPaginationUtils.getById(SERIAL_ID_FOR_3_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_3_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "ok async util for 2-page data"() {
        when:
        List<Episode> episodesOfSerial = okAsyncPaginationUtils.getById(SERIAL_ID_FOR_2_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_2_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "ok async util for 1-page data"() {
        when:
        List<Episode> episodesOfSerial = okAsyncPaginationUtils.getById(SERIAL_ID_FOR_1_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_1_PAGE_DATA
        where:
        i << (1..REPEAT)
    }
    /*Standard implementation by OkHTTP library*/

    def "ok standard util for 4-page data"() {
        when:
        List<Episode> episodesOfSerial = okPaginationUtils.getById(SERIAL_ID_FOR_4_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_4_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "ok standard util for 3-page data"() {
        when:
        List<Episode> episodesOfSerial = okPaginationUtils.getById(SERIAL_ID_FOR_3_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_3_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "ok standard util for 2-page data"() {
        when:
        List<Episode> episodesOfSerial = okPaginationUtils.getById(SERIAL_ID_FOR_2_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_2_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

    def "ok standard util for 1-page data"() {
        when:
        List<Episode> episodesOfSerial = okPaginationUtils.getById(SERIAL_ID_FOR_1_PAGE_DATA)
        then:
        episodesOfSerial.size() == EPISODES_COUNT_FOR_1_PAGE_DATA
        where:
        i << (1..REPEAT)
    }

}
