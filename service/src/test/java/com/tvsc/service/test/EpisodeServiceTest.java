package com.tvsc.service.test;

import com.tvsc.core.AppProfiles;
import com.tvsc.core.model.Episode;
import com.tvsc.service.config.ServiceConfig;
import com.tvsc.service.services.EpisodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

/**
 * @author Taras Zubrei
 */
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
public class EpisodeServiceTest {
    @Autowired
    private EpisodeService episodeService;

    @Test
    public void whenGetTheEpisodesCheckIfItIsNotNull() {
        Episode episode = episodeService.getEpisode(5L);
        assertThat(episode, is(notNullValue()));
    }

    @Test
    public void whenGetAllEpisodesOfTheSerialCheckIfCollectionIsNotEmpty() {
        List<Episode> episodesOfSerial = episodeService.getEpisodesOfSerial(78901L);
        assertThat(episodesOfSerial, is(not(empty())));
    }
}
