package com.tvsc.service

import com.tvsc.core.model.Episode
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
interface EpisodeService {
    fun getEpisode(id: Long): CompletableFuture<Episode>
    fun getEpisodesOfSerial(serialId: Long): CompletableFuture<List<Episode>>
    fun getWatchedEpisodes(serialId: Long): CompletableFuture<List<Long>>
    fun setWatchedEpisodes(serialId: Long, episodes: List<Long>): CompletableFuture<Void>
}