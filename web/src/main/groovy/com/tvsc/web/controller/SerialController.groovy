package com.tvsc.web.controller

import com.tvsc.service.EpisodeService
import com.tvsc.service.SerialService
import com.tvsc.web.EpisodeDto
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 *
 * @author Taras Zubrei
 */
@RestController
@RequestMapping('api/v1/series')
class SerialController {
    @Autowired
    private SerialService serialService
    @Autowired
    private EpisodeService episodeService
    @Autowired
    ModelMapper modelMapper

    @RequestMapping(path = '/{id}', method = RequestMethod.GET)
    def findOne(@PathVariable Long id) {
        serialService.getSerial(id).thenApply { it.convert() }
    }

    @RequestMapping(path = '/{id}/info', method = RequestMethod.GET)
    def findInfo(@PathVariable Long id) {
        serialService.getSerialInfo(id).thenApply { it.convert() }
    }

    @RequestMapping(path = '/{id}/episodes', method = RequestMethod.GET)
    def findEpisodesOfSerial(@PathVariable Long id) {
        episodeService.getEpisodesOfSerial(id).thenApply { it.collect { it.convert() } }
    }

    @RequestMapping(path = '/{id}/episodes/watched', method = RequestMethod.GET)
    def findWatchedEpisodesOfSerial(@PathVariable Long id) {
        episodeService.getWatchedEpisodes(id).thenApply { it.collect { new EpisodeDto(id: it) } }
    }

    @RequestMapping(method = RequestMethod.GET)
    def findAll() {
        serialService.restoreAllData().thenApply { it.collect { it.convert() } }
    }

    @RequestMapping(path = '/{id}', method = RequestMethod.POST)
    def void add(@PathVariable Long id) {
        serialService.addSerial(id)
    }

    @RequestMapping(path = '/{id}/episodes', method = RequestMethod.PATCH)
    def void findWatchedEpisodesOfSerial(@PathVariable Long id, @RequestBody List<Long> watched) {
        episodeService.setWatchedEpisodes(id, watched)
    }

    @RequestMapping(path = '/{id}', method = RequestMethod.DELETE)
    def void delete(@PathVariable Long id) {
        serialService.deleteSerial(id)
    }
}
