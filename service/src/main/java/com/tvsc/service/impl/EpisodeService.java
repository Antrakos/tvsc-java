package com.tvsc.service.impl;

import com.tvsc.core.model.Episode;
import com.tvsc.persistence.repository.EpisodeRepository;
import com.tvsc.service.Constants;
import com.tvsc.service.utils.HttpUtils;
import com.tvsc.service.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Service
public class EpisodeService {
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private JsonUtils jsonUtils;

    public Episode getEpisode(Long id) {
        return jsonUtils.getSingleObject(httpUtils.get(Constants.EPISODES + id), Episode.class);
    }

    public List<Episode> getEpisodesOfSerial(Long serialId) {
        return httpUtils.getFullResponse(Constants.SERIES + serialId + "/episodes", Episode.class);
    }

    public List<Long> getWatchedEpisodes(Long serialId) {
        return episodeRepository.getEpisodes(serialId, userService.getCurrentUser().getId());
    }

    public void setWatchedEpisodes(Long serialId, List<Long> episodes) {
        episodeRepository.setWatched(userService.getCurrentUser().getId(), serialId, episodes);
    }
}
