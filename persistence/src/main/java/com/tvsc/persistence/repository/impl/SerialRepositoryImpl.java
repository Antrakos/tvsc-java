package com.tvsc.persistence.repository.impl;

import com.tvsc.persistence.repository.SerialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Repository
@Transactional
public class SerialRepositoryImpl implements SerialRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> getSeries(Long userId) {
        return jdbcTemplate.query("SELECT serial_id FROM users_series_mapping WHERE user_id=?", new Object[]{userId}, (rs, rowNum) -> {
            return rs.getLong(1);
        });
    }

    @Override
    public void add(Long userId, Long serialId) {
        jdbcTemplate.update("INSERT INTO users_series_mapping VALUES (?, ?)", userId, serialId);
    }

    @Override
    public void delete(Long userId, Long serialId) {
        jdbcTemplate.update("DELETE FROM users_episodes_mapping WHERE user_id=? AND serial_id=?", userId, serialId);
        jdbcTemplate.update("DELETE FROM users_series_mapping WHERE user_id=? AND serial_id=?", userId, serialId);
    }
}
