package com.tvsc.service.impl

import com.tvsc.core.immutable.Episode
import com.tvsc.persistence.repository.EpisodeRepository
import com.tvsc.service.Constants
import com.tvsc.service.EpisodeService
import com.tvsc.service.UserService
import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import com.tvsc.service.util.PaginationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.Observable
import rx.Single
import rx.lang.kotlin.deferredObservable
import rx.lang.kotlin.toObservable

/**
 *
 * @author Taras Zubrei
 */
@Component
open class EpisodeServiceImpl @Autowired constructor(val httpUtils: HttpUtils,
                                                     val jsonUtils: JsonUtils,
                                                     val paginationUtils: PaginationUtils,
                                                     val episodeRepository: EpisodeRepository,
                                                     val userService: UserService) : EpisodeService {

    override fun getEpisode(id: Long): Single<Episode> =
            httpUtils.get(Constants.EPISODES + id)
                    .map { jsonUtils.getSingleObject(it, Episode::class.java) }


    override fun getEpisodesOfSerial(serialId: Long): Observable<Episode> {
        return paginationUtils.getFullResponse(Constants.SERIES + serialId + "/episodes", Episode::class.java)
    }

    override fun getWatchedEpisodes(serialId: Long): Observable<Long> =
            episodeRepository.getEpisodes(userService.getCurrentUser().id(), serialId).toObservable()

    override fun setWatchedEpisodes(serialId: Long, episodes: List<Long>): Single<Unit> = Single.create {
        it.onSuccess(episodeRepository.setWatched(userService.getCurrentUser().id(), serialId, episodes))
    }
}