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

    @Shared
    List<Episode> expected = [
            new Episode()
                    .setId(5598674L)
                    .setFirstAired(LocalDate.of(2016, 5, 24))
                    .setImage('episodes/279121/5598674.jpg')
                    .setImdbId('tt5215758')
                    .setName('The Race of His Life')
                    .setOverview('Barry vows to stop Zoom after learning Zoom\'s true plans.')
                    .setNumber(23)
                    .setSeason(2)
                    .setRating(8.0)
                    .setWatched(false),
            new Episode()
                    .setId(4894849L)
                    .setName('Hot wheels')
                    .setOverview('Barry tries to stop Zoom.')
                    .setNumber(23)
                    .setSeason(2)
    ] as List<Episode>

    def "single object"() {
        given:
        String json = """{"data": {"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8}}"""
        when:
        Episode episode = jsonUtils.getSingleObject(json, Episode)
        then:
        episode == expected[0]
    }

    def "list of objects"() {
        given:
        String json = """{"links": {"first":1, "last":1}, "data": [{"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8},{"id": 4894849,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "Hot wheels","overview": "Barry tries to stop Zoom."}]}"""
        when:
        List<Episode> episodes = jsonUtils.getListData(json, Episode)
        then:
        episodes == expected
    }

    def "page"() {
        given:
        String json = """{"links": {"first":1, "last":1}, "data": [{"id": 5598674,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "The Race of His Life","firstAired": "2016-05-24","overview": "Barry vows to stop Zoom after learning Zoom's true plans.","lastUpdated": 1466154212,"filename": "episodes/279121/5598674.jpg","seriesId": 279121,"imdbId": "tt5215758","siteRating": 8},{"id": 4894849,"airedSeason": 2,"airedEpisodeNumber": 23,"episodeName": "Hot wheels","overview": "Barry tries to stop Zoom."}]}"""
        when:
        Pair<List<Episode>, Integer> episodes = jsonUtils.getPage(json, Episode)
        then:
        episodes.first == expected
        episodes.second == 1
    }
}
