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

    override fun getEpisode(id: Long): CompletableFuture<Episode> {
        return httpUtils.get(Constants.EPISODES + id).thenApply { jsonUtils.getSingleObject(it, Episode::class.java) }
    }

    override fun getEpisodesOfSerial(serialId: Long): CompletableFuture<List<Episode>> {
        return paginationUtils.getFullResponse(Constants.SERIES + serialId + "/episodes", Episode::class.java)
    }

    override fun getWatchedEpisodes(serialId: Long): CompletableFuture<List<Long>> {
        return CompletableFuture.supplyAsync { episodeRepository.getEpisodes(userService.getCurrentUser().id, serialId) }
    }

    override fun setWatchedEpisodes(serialId: Long, episodes: List<Long>) = CompletableFuture.runAsync { episodeRepository.setWatched(userService.getCurrentUser().id, serialId, episodes) }
}