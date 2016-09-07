package com.tvsc.web.controller;

import com.tvsc.core.model.Episode;
import com.tvsc.core.model.Serial;
import com.tvsc.service.EpisodeService;
import com.tvsc.service.SerialService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rx.Observable;
import rx.Single;

import java.util.List;

import static com.tvsc.web.Routes.SERIES;

/**
 * @author Antrakos
 */
@RestController
@RequestMapping(SERIES)
public class SerialController {
    @Autowired
    private SerialService serialService;
    @Autowired
    private EpisodeService episodeService;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Single<Serial> findOne(@PathVariable Long id) {
        return serialService.getSerial(id);
    }

    @RequestMapping(path = "/{id}/info", method = RequestMethod.GET)
    public Single<Serial> findInfo(@PathVariable Long id) {
        return serialService.getSerialInfo(id);
    }

    @RequestMapping(path = "/{id}/episodes", method = RequestMethod.GET)
    public Observable<Episode> findEpisodesOfSerial(@PathVariable Long id) {
        return episodeService.getEpisodesOfSerial(id);
    }

    @RequestMapping(path = "/{id}/episodes/watched", method = RequestMethod.GET)
    public Observable<EpisodeEntry> findWatchedEpisodesOfSerial(@PathVariable Long id) {
        return episodeService.getWatchedEpisodes(id).map(EpisodeEntry::new);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Observable<Serial> findAll() {
        return serialService.restoreAllData();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@PathVariable Long id) {
        serialService.addSerial(id);
    }

    @RequestMapping(path = "/{id}/episodes", method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public void findWatchedEpisodesOfSerial(@PathVariable Long id, @RequestBody List<Long> watched) {
        episodeService.setWatchedEpisodes(id, watched);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        serialService.deleteSerial(id);
    }

    @Data
    private class EpisodeEntry {
        private final Long id;
    }
}
