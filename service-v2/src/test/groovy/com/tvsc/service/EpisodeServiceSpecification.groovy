package com.tvsc.service

import com.tvsc.core.AppProfiles
import com.tvsc.core.model.Episode
import com.tvsc.service.config.ServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.LocalDate
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = ServiceConfig)
@ActiveProfiles(AppProfiles.TEST)
@Transactional
class EpisodeServiceSpecification extends Specification {
    @Autowired
    EpisodeService episodeService

    def "given future of episode when get the object then check firstAired date"() {
        given:
        CompletableFuture<Episode> completableFuture = episodeService.getEpisode(5598674L)
        when:
        Episode episode = completableFuture.join()
        then:
        episode.firstAired == LocalDate.of(2016, 5, 24)
    }

    def "given future of episode list when get the list then assure that all episodes' firstAired date is after first episode one"() {
        given:
        CompletableFuture<List<Episode>> completableFuture = episodeService.getEpisodesOfSerial(279121L)
        when:
        List<Episode> episodes = completableFuture.join()
        then:
        episodes.stream().allMatch { LocalDate.of(2005, 9, 12).isBefore(it.firstAired) }
    }

    def "given unwatched episodes when set them watched then check if operation is successful"() {
        given:
        def ABSENT_IDS = [4426795L, 5487129L, 362532L, 427739L]
        def old = episodeService.getWatchedEpisodes(78901L).join().size()
        when:
        episodeService.setWatchedEpisodes(78901L, ABSENT_IDS)
        then:
        def present = episodeService.getWatchedEpisodes(78901L).join().size()
        present - old == ABSENT_IDS.size()
    }
}
