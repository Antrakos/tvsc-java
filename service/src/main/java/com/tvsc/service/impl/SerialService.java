package com.tvsc.service.impl;

import com.tvsc.core.model.BannerInfo;
import com.tvsc.core.model.Episode;
import com.tvsc.core.model.Season;
import com.tvsc.core.model.Serial;
import com.tvsc.persistence.repository.SerialRepository;
import com.tvsc.service.Constants;
import com.tvsc.service.utils.AsyncHttpUtils;
import com.tvsc.service.utils.HttpUtils;
import com.tvsc.service.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Taras Zubrei
 */
@Service
public class SerialService {
    @Autowired
    private EpisodeService episodeService;
    @Autowired
    private JsonUtils jsonUtils;
    @Autowired
    private SerialRepository serialRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private AsyncHttpUtils asyncHttpUtils;

    public Serial getSerialInfo(Long id) {
        return jsonUtils.getSingleObject(httpUtils.get(Constants.SERIES + id), Serial.class);
    }

    public Serial getSerial(Long id) {
        final CompletableFuture<String> poster = asyncHttpUtils.get(Constants.SERIES + id + "/images/query?keyType=poster")
                .thenApply(response -> jsonUtils.getListData(response, BannerInfo.class)
                        .stream()
                        .max(BannerInfo::compareTo)
                        .map(BannerInfo::getFileName)
                        .orElse(null));
        final CompletableFuture<Map<Integer, String>> banners = asyncHttpUtils.get(Constants.SERIES + id + "/images/query?keyType=season").thenApply(response -> jsonUtils.getListData(response, BannerInfo.class)
                .stream()
                .collect(Collectors.groupingBy(BannerInfo::getKey))
                .values()
                .stream()
                .map(list -> list.stream().max(BannerInfo::compareTo).orElse(null))
                .filter(bannerInfo -> bannerInfo != null)
                .filter(bannerInfo -> bannerInfo.getKey() != null)
                .collect(Collectors.toMap(banner -> Integer.valueOf(banner.getKey()), BannerInfo::getFileName)));

        final Serial serial = jsonUtils.getSingleObject(httpUtils.get(Constants.SERIES + id), Serial.class);
        serial.setPoster(poster.join());
        final List<Season> seasons = episodeService
                .getEpisodesOfSerial(id)
                .stream()
                .collect(Collectors.groupingBy(Episode::getSeason))
                .entrySet()
                .stream()
                .map(entry -> {
                    Season season = new Season();
                    season.setNumber(entry.getKey());
                    season.setBanner(banners.join().get(entry.getKey()));
                    season.setEpisodes(entry.getValue());
                    return season;
                })
                .collect(Collectors.toList());
        serial.setSeasons(seasons);
        return serial;
    }

    public List<Serial> restoreAllData() {
        final List<Serial> series = serialRepository.getSeries(userService.getCurrentUser().getId())
                .stream()
                .map(this::getSerial)
                .collect(Collectors.toList());
        for (Serial serial : series) {
            final List<Long> watchedEpisodes = episodeService.getWatchedEpisodes(serial.getId());

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
