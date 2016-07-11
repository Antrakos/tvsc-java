package com.tvsc.persistence.repository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Taras Zubrei
 */
public interface SerialRepository {
    List<Long> getSeries(Long userId);

    Long count(Long userId);

    void add(Long userId, Long serialId);

    void delete(Long userId, Long serialId);
}
