package com.tvsc.web.controller

import com.tvsc.service.impl.EpisodeService
import com.tvsc.web.EpisodeDto
import org.modelmapper.ModelMapper
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
@RequestMapping('api/v1/episodes')
class EpisodeController {
    @Autowired
    private EpisodeService episodeService
    @Autowired
    ModelMapper modelMapper

    @RequestMapping(path = '/{id}', method = RequestMethod.GET)
    def findOne(@PathVariable Long id) {
        episodeService.getEpisode(id).collect { modelMapper.map(it, EpisodeDto) }
    }
}
