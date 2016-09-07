package com.tvsc.web.controller;

import com.tvsc.core.model.Episode;
import com.tvsc.service.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rx.Single;

import static com.tvsc.web.Routes.EPISODES;

/**
 * @author Antrakos
 */
@RestController
@RequestMapping(EPISODES)
public class EpisodeController {
    @Autowired
    private EpisodeService episodeService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Single<Episode> findOne(@PathVariable Long id) {
        return episodeService.getEpisode(id);
    }
}
