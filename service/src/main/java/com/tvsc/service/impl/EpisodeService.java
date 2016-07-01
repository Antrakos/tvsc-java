package com.tvsc.service.impl;

import com.tvsc.core.model.Episode;
import com.tvsc.persistence.repository.EpisodeRepository;
import com.tvsc.service.Constants;
import com.tvsc.service.utils.HttpUtils;
import com.tvsc.service.utils.JsonUtils;
import com.tvsc.service.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Service
public class EpisodeService {
    private EpisodeRepository episodeRepository;
    private UserService userService;
    private HttpUtils httpUtils;
    private JsonUtils jsonUtils;
    private PaginationUtils paginationUtils;

    @Autowired
    public EpisodeService(EpisodeRepository episodeRepository, UserService userService, HttpUtils httpUtils, JsonUtils jsonUtils, PaginationUtils paginationUtils) {
        this.episodeRepository = episodeRepository;
        this.userService = userService;
        this.httpUtils = httpUtils;
        this.jsonUtils = jsonUtils;
        this.paginationUtils = paginationUtils;
    }

    public Episode getEpisode(Long id) {
        return jsonUtils.getSingleObject(httpUtils.get(Constants.EPISODES + id), Episode.class);
    }

    public List<Episode> getEpisodesOfSerial(Long serialId) {
        return paginationUtils.getFullResponse(Constants.SERIES + serialId + "/episodes", Episode.class);
    }

    public List<Long> getWatchedEpisodes(Long serialId) {
        return episodeRepository.getEpisodes(serialId, userService.getCurrentUser().getId());
    }

    public void setWatchedEpisodes(Long serialId, List<Long> episodes) {
        episodeRepository.setWatched(userService.getCurrentUser().getId(), serialId, episodes);
    }
}
