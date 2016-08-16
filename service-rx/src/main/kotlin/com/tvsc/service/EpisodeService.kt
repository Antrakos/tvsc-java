package com.tvsc.service

import com.tvsc.core.immutable.Episode
import rx.Observable
import rx.Single

/**
 *
 * @author Taras Zubrei
 */
interface EpisodeService {
    fun getEpisode(id: Long): Single<Episode>
    fun getEpisodesOfSerial(serialId: Long): Observable<Episode>
    fun getWatchedEpisodes(serialId: Long): Observable<Long>
    fun setWatchedEpisodes(serialId: Long, episodes: List<Long>): Single<Unit>
}