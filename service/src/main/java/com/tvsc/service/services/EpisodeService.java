package com.tvsc.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stefanbirkner.fishbowl.Fishbowl;
import com.tvsc.core.model.Episode;
import com.tvsc.persistence.repository.EpisodeRepository;
import com.tvsc.service.Constants;
import com.tvsc.service.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Service
public class EpisodeService {
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EpisodeRepository episodeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpUtils httpUtils;

    public Episode getEpisode(Long id) {
        HttpGet request = new HttpGet(Constants.EPISODES + id);
        HttpResponse response = Fishbowl.wrapCheckedException(() -> httpClient.execute(request));
        request.releaseConnection();
        return Fishbowl.wrapCheckedException(() -> (Episode) objectMapper
                .reader()
                .forType(Episode.class)
                .withRootName(Constants.ROOT)
                .readValue(response.getEntity().getContent()));
    }

    public List<Episode> getEpisodesOfSerial(Long serialId) {
        return Fishbowl.wrapCheckedException(() -> httpUtils.getFullResponse(
                Constants.SERIES + serialId + "/episodes",
                Episode.class));
    }

    public List<Long> getWatchedEpisodes(Long serialId) {
        return episodeRepository.getEpisodes(serialId, userService.getCurrentUser().getId());
    }

    public void setWatchedEpisodes(Long serialId, List<Long> episodes) {
        episodeRepository.setWatched(userService.getCurrentUser().getId(), serialId, episodes);
    }
}
