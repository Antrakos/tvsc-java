package com.tvsc.service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stefanbirkner.fishbowl.Fishbowl;
import com.tvsc.core.model.BannerInfo;
import com.tvsc.core.model.Episode;
import com.tvsc.core.model.Season;
import com.tvsc.core.model.Serial;
import com.tvsc.persistence.repository.SerialRepository;
import com.tvsc.service.Constants;
import com.tvsc.service.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Taras Zubrei
 */
@Service
public class SerialService {
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EpisodeService episodeService;
    @Autowired
    private JsonUtils jsonUtils;
    @Autowired
    private SerialRepository serialRepository;
    @Autowired
    private UserService userService;

    public Serial getSerialInfo(Long id) {
        HttpGet request = new HttpGet(Constants.SERIES + id);
        String response = Fishbowl.wrapCheckedException(() -> IOUtils.toString(httpClient.execute(request).getEntity().getContent(), "utf-8"));
        Serial serial = Fishbowl.wrapCheckedException(() -> (Serial) objectMapper.reader().forType(Serial.class).withRootName(Constants.ROOT).readValue(response));
        request.releaseConnection();
        return serial;
    }

    public Serial getSerial(Long id) {
        final HttpGet requestSeries = new HttpGet(Constants.SERIES + id);
        final HttpResponse responseSeries = Fishbowl.wrapCheckedException(() -> httpClient.execute(requestSeries));
        Serial serial = Fishbowl.wrapCheckedException(() -> (Serial) objectMapper.reader().forType(Serial.class).withRootName(Constants.ROOT).readValue(responseSeries.getEntity().getContent()));
        requestSeries.releaseConnection();

        final HttpGet requestBanners = new HttpGet(Constants.SERIES + id + "/images/query?keyType=season");
        final HttpResponse responseBanners = Fishbowl.wrapCheckedException(() -> httpClient.execute(requestBanners));
        List<BannerInfo> seasonBanners = Fishbowl.wrapCheckedException(() -> jsonUtils.getListData(IOUtils.toString(responseBanners.getEntity().getContent(), "utf-8"), BannerInfo.class));
        requestBanners.releaseConnection();

        final HttpGet requestPosters = new HttpGet(Constants.SERIES + id + "/images/query?keyType=poster");
        final HttpResponse responsePosters = Fishbowl.wrapCheckedException(() ->httpClient.execute(requestPosters));
        final String poster = Fishbowl.wrapCheckedException(() -> jsonUtils.getListData(IOUtils.toString(responsePosters.getEntity().getContent(), "utf-8"), BannerInfo.class))
                .stream()
                .max(BannerInfo::compareTo)
                .map(BannerInfo::getFileName)
                .get(); //TODO: null safety
        serial.setPoster(poster);
        requestPosters.releaseConnection();

        final Map<Integer, String> banners = seasonBanners.stream()
                .collect(Collectors.groupingBy(BannerInfo::getKey))
                .values()
                .stream()
                .map(list -> list.stream().max(BannerInfo::compareTo).get()) //TODO: null safety
                .collect(Collectors.toMap(banner -> Integer.valueOf(banner.getKey()), BannerInfo::getFileName));
        final List<Season> seasons = episodeService
                .getEpisodesOfSerial(id)
                .stream()
                .collect(Collectors.groupingBy(Episode::getSeason))
                .entrySet()
                .stream()
                .map(entry -> {
                    Season season = new Season();
                    season.setNumber(entry.getKey());
                    season.setBanner(banners.get(entry.getKey()));
                    season.setEpisodes(entry.getValue());
                    return season;
                })
                .collect(Collectors.toList());
        serial.setSeasons(seasons);
        return serial;
    }

    public List<Serial> restoreAllData() {
        List<Serial> series = serialRepository.getSeries(userService.getCurrentUser().getId())
                .stream()
                .map(this::getSerial)
                .collect(Collectors.toList());
        for (Serial serial : series) {
            List<Long> watchedEpisodes = episodeService.getWatchedEpisodes(serial.getId());

            serial.getSeasons().stream()
                    .flatMap(season -> season.getEpisodes().stream())
                    .filter(episode -> watchedEpisodes.contains(episode.getId()))
                    .forEach(episode -> episode.setWatched(true));

        }
        return series;
    }

    public void addSerial(Long id) {
        serialRepository.add(userService.getCurrentUser().getId(), id);
    }

    public void deleteSerial(Long id) {
        serialRepository.delete(userService.getCurrentUser().getId(), id);
    }
}
