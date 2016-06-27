package com.tvsc.service.test;

import com.tvsc.core.model.Episode;
import com.tvsc.service.config.ServiceConfig;
import com.tvsc.service.services.EpisodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @author Taras Zubrei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceConfig.class)
public class EpisodeServiceTest {
    @Autowired
    private EpisodeService episodeService;

    @Test
    public void getEpisode() {
        System.out.println(episodeService.getEpisode(5L));
        episodeService.getEpisodesOfSerial(78901L).stream().map(Episode::getId).forEach(System.out::println);
    }
}
