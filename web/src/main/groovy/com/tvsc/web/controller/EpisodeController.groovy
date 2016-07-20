package com.tvsc.web.controller

import com.tvsc.service.EpisodeService
import com.tvsc.web.Routes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author Taras Zubrei
 */
@RestController
@RequestMapping(Routes.EPISODES)
class EpisodeController {
    @Autowired
    private EpisodeService episodeService

    @RequestMapping(path = '/{id}', method = RequestMethod.GET)
    def findOne(@PathVariable Long id) {
        episodeService.getEpisode(id).thenApply { it.convert() }
    }
}
