package com.tvsc.persistence.repository;

import java.util.List;

/**
 * @author Taras Zubrei
 */
public interface EpisodeRepository {
    List<Long> getEpisodes(Long userId, Long serialId);

    void setWatched(Long userId, Long serialId, List<Long> episodes);

    void setUnwatched(Long userId, Long serialId, List<Long> episodes);
}
