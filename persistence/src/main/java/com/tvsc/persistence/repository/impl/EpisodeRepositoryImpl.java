package com.tvsc.persistence.repository.impl;

import com.tvsc.persistence.exception.PersistenceException;
import com.tvsc.persistence.repository.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.BatchUpdateException;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

/**
 * @author Taras Zubrei
 */
@Repository
@Transactional
public class EpisodeRepositoryImpl implements EpisodeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> getEpisodes(Long userId, Long serialId) {
        return jdbcTemplate.query("SELECT episode_id FROM users_episodes_mapping WHERE user_id=? AND serial_id=?", new Object[]{userId, serialId}, (rs, rowNum) -> {
            return rs.getLong(1);
        });
    }

    @Override
    public void setWatched(Long userId, Long serialId, List<Long> episodes) {
        episodes.forEach(id -> jdbcTemplate.update("INSERT INTO users_episodes_mapping  VALUES(?, ?, ?);", userId, id, serialId));
    }

    @Override
    public void setUnwatched(Long userId, Long serialId, List<Long> episodes) {
        int[] result = jdbcTemplate.batchUpdate(episodes.stream().map(id -> String.format("DELETE FROM users_episodes_mapping  WHERE user_id=%d AND episode_id=%d AND serial_id=%d", userId, id, serialId)).toArray(String[]::new));
        OptionalInt fail = Arrays.stream(result).filter(res -> res == 0).findAny();
        if (fail.isPresent())
            throw new PersistenceException("Failed to execute update", new BatchUpdateException(result));
    }
}
