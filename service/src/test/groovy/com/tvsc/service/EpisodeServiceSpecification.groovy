package com.tvsc.service

import com.tvsc.core.AppProfiles
import com.tvsc.core.model.Episode
import com.tvsc.service.config.ServiceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 *
 * @author Taras Zubrei
 */
@ContextConfiguration(classes = ServiceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
class EpisodeServiceSpecification extends Specification {
    @Autowired
    private episodeService


    def "when get the episodes then check if it is not null"() {
        when:
        Episode episode = episodeService.getEpisode(5L)
        then:
        episode.id == 5L
    }

    def "when get all episodes of the serial then check if collection is not empty"() {
        when:
        List<Episode> episodesOfSerial = episodeService.getEpisodesOfSerial(78901L)
        then:
        episodesOfSerial.size() == 245
    }
}
