package com.tvsc.service.impl;

import com.tvsc.core.model.BannerInfo;
import com.tvsc.core.model.Episode;
import com.tvsc.core.model.Season;
import com.tvsc.core.model.Serial;
import com.tvsc.persistence.repository.SerialRepository;
import com.tvsc.service.Constants;
import com.tvsc.service.utils.HttpUtils;
import com.tvsc.service.utils.JsonUtils;
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
    private EpisodeService episodeService;
    @Autowired
    private JsonUtils jsonUtils;
    @Autowired
    private SerialRepository serialRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HttpUtils httpUtils;

    public Serial getSerialInfo(Long id) {
        return jsonUtils.getSingleObject(httpUtils.get(Constants.SERIES + id), Serial.class);
    }

    public Serial getSerial(Long id) {
        Serial serial = jsonUtils.getSingleObject(httpUtils.get(Constants.SERIES + id), Serial.class);
        List<BannerInfo> seasonBanners = jsonUtils.getListData(httpUtils.get(Constants.SERIES + id + "/images/query?keyType=season"), BannerInfo.class);

        final String poster = jsonUtils.getListData(httpUtils.get(Constants.SERIES + id + "/images/query?keyType=poster"), BannerInfo.class)
                .stream()
                .max(BannerInfo::compareTo)
                .map(BannerInfo::getFileName)
                .orElse(null);
        serial.setPoster(poster);

        final Map<Integer, String> banners = seasonBanners.stream()
                .collect(Collectors.groupingBy(BannerInfo::getKey))
                .values()
                .stream()
                .map(list -> list.stream().max(BannerInfo::compareTo).orElse(null))
                .filter(bannerInfo -> bannerInfo != null)
                .filter(bannerInfo -> bannerInfo.getKey() != null)
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
