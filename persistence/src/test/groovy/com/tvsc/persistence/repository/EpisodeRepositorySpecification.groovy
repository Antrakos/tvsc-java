package com.tvsc.persistence.repository

import com.tvsc.core.AppProfiles
import com.tvsc.persistence.exception.PersistenceException
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.sql.BatchUpdateException

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration("classpath*:PersistenceConfig.groovy")
@ActiveProfiles(AppProfiles.TEST)
@Slf4j
@Transactional
class EpisodeRepositorySpecification extends Specification {
    @Autowired
    private EpisodeRepository episodeRepository

    private final USER_ID = 1L
    private final PRESENT_IDS = [5378093L, 300428L, 312249L, 405398L, 3039311L]
    private final ABSENT_IDS = [4426795L, 5487129L, 362532L, 427739L]

    def "when get all episodes then expect list of watched episodes' ids"() {
        when:
        List<Long> episodes = episodeRepository.getEpisodes(USER_ID, 78901L);
        then:
        episodes.size() == 19
    }

    def "given all watched episodes when set watched the new ones then expect their presence"() {
        given:
        List<Long> episodes = episodeRepository.getEpisodes(USER_ID, 78901L);
        when:
        episodeRepository.setWatched(USER_ID, 78901L, ABSENT_IDS);
        then:
        (episodes + ABSENT_IDS).toSet() == episodeRepository.getEpisodes(USER_ID, 78901L).toSet()
    }

    def "given all watched episodes when set unwatched some of them then expect their absence"() {
        given:
        List<Long> episodes = episodeRepository.getEpisodes(USER_ID, 78901L);
        when:
        episodeRepository.setUnwatched(USER_ID, 78901L, PRESENT_IDS);
        then:
        (episodes - PRESENT_IDS).toSet() == episodeRepository.getEpisodes(USER_ID, 78901L).toSet()
    }

    def "when set watched already watched episode then expect DuplicatedKeyException"() {
        when:
        episodeRepository.setWatched(USER_ID, 78901L, PRESENT_IDS)
        then:
        thrown(DuplicateKeyException.class)
    }

    def "when set unwatched yet not watched episode then expect PersistenceException with BatchUpdateException as cause"() {
        when:
        episodeRepository.setUnwatched(USER_ID, 78901L, ABSENT_IDS)
        then:
        def ex = thrown(PersistenceException.class)
        ex.cause.class == BatchUpdateException.class
    }
}
