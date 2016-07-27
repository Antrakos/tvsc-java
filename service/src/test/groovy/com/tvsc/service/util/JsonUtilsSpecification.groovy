package com.tvsc.service.util

import com.tvsc.core.model.Episode
import com.tvsc.service.config.ServiceConfig
import com.tvsc.service.util.impl.JsonUtilsImpl
import kotlin.Pair
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

/**
 *
 * @author Taras Zubrei
 */
class JsonUtilsSpecification extends Specification {
    @Shared
    JsonUtils jsonUtils = new JsonUtilsImpl(new ServiceConfig().objectMapper())
//    JsonUtils jsonUtils = new GsonUtilsImpl(new ServiceConfig().gson())

    @Shared
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

    def "single object"() {
        given:
        String json = """{"data": {"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8}}"""
        when:
        Episode episode = jsonUtils.getSingleObject(new StringReader(json), Episode)
        then:
        episode == expected[0]
    }

    def "list of objects"() {
        given:
        String json = """{"links": {"first":1, "last":1}, "data": [{"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8},{"id": 4894849,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "Hot wheels","overview": "Barry tries to stop Zoom."}]}"""
        when:
        List<Episode> episodes = jsonUtils.getListData(new StringReader(json), Episode)
        then:
        episodes == expected
    }

    def "page"() {
        given:
        String json = """{"links": {"first":1, "last":1}, "data": [{"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8},{"id": 4894849,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "Hot wheels","overview": "Barry tries to stop Zoom."}]}"""
        when:
        Pair<List<Episode>, Integer> episodes = jsonUtils.getPage(new StringReader(json), Episode)
        then:
        episodes.first == expected
        episodes.second == 1
    }
}
