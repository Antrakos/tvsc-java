package com.tvsc.service.util

import com.tvsc.core.AppProfiles
import com.tvsc.core.model.Episode
import com.tvsc.service.config.ServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = ServiceConfig)
@ActiveProfiles(AppProfiles.TEST)
class JsonUtilsSpecification extends Specification {
    @Autowired
    JsonUtils jsonUtils

    def "single object"() {
        given:
        Episode expected = new Episode(id: 5598674L,
                firstAired: LocalDate.of(2016, 5, 24),
                image: 'episodes/279121/5598674.jpg',
                imdbId: 'tt5215758',
                name: 'The Race of His Life',
                overview: 'Barry vows to stop Zoom after learning Zoom\'s true plans.',
                number: 23,
                season: 2,
                rating: 8.0,
                watched: false)
        String json = """{"data": {"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8}}"""
        when:
        Episode episode = jsonUtils.getSingleObject(json, Episode)
        then:
        expected == episode
    }

    def "list of objects"() {
        given:
        List<Episode> expected = [
                new Episode(id: 5598674L,
                        firstAired: LocalDate.of(2016, 5, 24),
                        image: 'episodes/279121/5598674.jpg',
                        imdbId: 'tt5215758',
                        name: 'The Race of His Life',
                        overview: 'Barry vows to stop Zoom after learning Zoom\'s true plans.',
                        number: 23,
                        season: 2,
                        rating: 8.0,
                        watched: false),
                new Episode(id: 4894849L,
                        name: 'Hot wheels',
                        overview: 'Barry tries to stop Zoom.',
                        number: 23,
                        season: 2)
        ]
        String json = """{"data": [{"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8},{"id": 4894849,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "Hot wheels","overview": "Barry tries to stop Zoom."}]}"""
        when:
        List<Episode> episodes = jsonUtils.getListData(json, Episode)
        then:
        episodes == expected
    }
}
