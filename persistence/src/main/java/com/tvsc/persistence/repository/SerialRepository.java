package com.tvsc.persistence.repository;

import java.util.List;

/**
 * @author Taras Zubrei
 */
public interface SerialRepository {
    List<Long> getSeries(Long userId);

    void add(Long userId, Long serialId);

    void delete(Long userId, Long serialId);
}
