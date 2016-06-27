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
    public void getEpisode() {
        System.out.println(episodeService.getEpisode(5L));
        episodeService.getEpisodesOfSerial(78901L).stream().map(Episode::getId).forEach(System.out::println);
    }
}
