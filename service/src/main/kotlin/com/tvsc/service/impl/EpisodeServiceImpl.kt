package com.tvsc.service.impl

import com.tvsc.core.model.Episode
import com.tvsc.persistence.repository.EpisodeRepository
import com.tvsc.service.Constants
import com.tvsc.service.EpisodeService
import com.tvsc.service.UserService
import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import com.tvsc.service.util.PaginationUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Supplier

/**
 *
 * @author Taras Zubrei
 */
@Component
open class EpisodeServiceImpl @Autowired constructor(val httpUtils: HttpUtils,
                                                     val jsonUtils: JsonUtils,
                                                     val paginationUtils: PaginationUtils,
                                                     val episodeRepository: EpisodeRepository,
                                                     val userService: UserService,
                                                     val executor: Executor) : EpisodeService {

    override fun getEpisode(id: Long): CompletableFuture<Episode> {
        return httpUtils.getReader(Constants.EPISODES + id).thenApply { jsonUtils.getSingleObject(it, Episode::class.java) }
    }

    override fun getEpisodesOfSerial(serialId: Long): CompletableFuture<List<Episode>> {
        return paginationUtils.getFullResponse(Constants.SERIES + serialId + "/episodes", Episode::class.java)
    }

    override fun getWatchedEpisodes(serialId: Long): CompletableFuture<List<Long>> = CompletableFuture.supplyAsync(Supplier {
        episodeRepository.getEpisodes(userService.getCurrentUser().id, serialId)
    }, executor)

    override fun setWatchedEpisodes(serialId: Long, episodes: List<Long>): CompletableFuture<Void> = CompletableFuture.runAsync(Runnable {
        episodeRepository.setWatched(userService.getCurrentUser().id, serialId, episodes)
    }, executor)
}