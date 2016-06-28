package com.tvsc.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvsc.core.exception.ExceptionUtil;
import com.tvsc.core.model.Episode;
import com.tvsc.persistence.repository.EpisodeRepository;
import com.tvsc.service.Constants;
import com.tvsc.service.exception.HttpException;
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
        final HttpGet request = new HttpGet(Constants.EPISODES + id);
        final HttpResponse response = ExceptionUtil.wrapCheckedException(() -> httpClient.execute(request), new HttpException(request));
        return ExceptionUtil.wrapCheckedException(() -> (Episode) objectMapper
                .reader()
                .forType(Episode.class)
                .withRootName(Constants.ROOT)
                .readValue(response.getEntity().getContent()), new HttpException(request));
    }

    public List<Episode> getEpisodesOfSerial(Long serialId) {
        return ExceptionUtil.wrapCheckedException(() -> httpUtils.getFullResponse(
                Constants.SERIES + serialId + "/episodes",
                Episode.class), new HttpException(new HttpGet(Constants.SERIES + serialId + "/episodes")));
    }

    public List<Long> getWatchedEpisodes(Long serialId) {
        return episodeRepository.getEpisodes(serialId, userService.getCurrentUser().getId());
    }

    public void setWatchedEpisodes(Long serialId, List<Long> episodes) {
        episodeRepository.setWatched(userService.getCurrentUser().getId(), serialId, episodes);
    }
}
