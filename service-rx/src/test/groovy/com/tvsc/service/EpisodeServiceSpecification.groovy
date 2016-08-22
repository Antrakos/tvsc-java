package com.tvsc.service

import com.tvsc.core.AppProfiles
import com.tvsc.core.model.Episode
import com.tvsc.core.model.User
import com.tvsc.persistence.repository.EpisodeRepository
import com.tvsc.service.config.ServiceConfig
import com.tvsc.service.impl.EpisodeServiceImpl
import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import com.tvsc.service.util.PaginationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional
import rx.Observable
import rx.Single
import spock.lang.Specification

import java.time.LocalDate

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
    HttpUtils httpUtils = Mock(HttpUtils)
    JsonUtils jsonUtils = Mock(JsonUtils)
    PaginationUtils paginationUtils = Mock(PaginationUtils)
    EpisodeRepository episodeRepository = Mock(EpisodeRepository)
    UserService userService = Mock(UserService)

    def "given future of episode list when get the list then assure that all episodes' firstAired date is after first episode one"() {
        given:
        Observable<Episode> observable = episodeService.getEpisodesOfSerial(279121L)
        when:
        List<Episode> episodes = observable.toList().toBlocking().toFuture().get()
        then:
        episodes.stream().allMatch { LocalDate.of(2005, 9, 12).isBefore(it.firstAired) }
    }

    def "given unwatched episodes when set them watched then check if operation is successful"() {
        given:
        def ABSENT_IDS = [4426795L, 5487129L, 362532L, 427739L]
        def old = episodeService.getWatchedEpisodes(78901L).count().toBlocking().toFuture().get()
        when:
        episodeService.setWatchedEpisodes(78901L, ABSENT_IDS).toBlocking().value()
        then:
        def present = episodeService.getWatchedEpisodes(78901L).count().toBlocking().toFuture().get()
        present - old == ABSENT_IDS.size()
    }

    def "given future of episode when get the object then check firstAired date"() {
        given:
        episodeService = new EpisodeServiceImpl(httpUtils, jsonUtils, paginationUtils, episodeRepository, userService)
        Episode expected = new Episode()
                .setId(5598674L)
                .setFirstAired(LocalDate.of(2016, 5, 24))
                .setImage('episodes/279121/5598674.setjpg')
                .setImdbId('tt5215758')
                .setName('The Race of His Life')
                .setOverview('Barry vows to stop Zoom after learning Zoom\'s true plans.')
                .setNumber(23)
                .setSeason(2)
                .setRating(8.0)
                .setWatched(false)

        String json = """{"data": {"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8}}"""
        httpUtils.get(_ as String) >> Single.just(json)
        jsonUtils.getSingleObject(json, Episode) >> expected
        userService.getCurrentUser() >> new User().setId(1L).setName('Jack')

        Single<Episode> single = episodeService.getEpisode(5598674L)
        when:
        Episode episode = single.toBlocking().value()
        then:
        episode == expected
    }
}
