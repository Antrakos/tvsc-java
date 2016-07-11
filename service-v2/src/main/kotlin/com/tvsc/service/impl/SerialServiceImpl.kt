package com.tvsc.service.impl

import com.tvsc.core.model.BannerInfo
import com.tvsc.core.model.Season
import com.tvsc.core.model.Serial
import com.tvsc.persistence.repository.SerialRepository
import com.tvsc.service.Constants
import com.tvsc.service.EpisodeService
import com.tvsc.service.SerialService
import com.tvsc.service.UserService
import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

/**
 *
 * @author Taras Zubrei
 */
@Component
open class SerialServiceImpl @Autowired constructor(val httpUtils: HttpUtils,
                                                    val jsonUtils: JsonUtils,
                                                    val serialRepository: SerialRepository,
                                                    val userService: UserService,
                                                    val episodeService: EpisodeService) : SerialService {

    override fun getSerialInfo(id: Long): CompletableFuture<Serial> = httpUtils.get(Constants.SERIES + id).thenApply { jsonUtils.getSingleObject(it, Serial::class.java) }

    override fun getSerial(id: Long): CompletableFuture<Serial> {

        val poster = httpUtils.get(Constants.SERIES + "$id/images/query?keyType=poster").thenApply {
            jsonUtils.getListData(it, BannerInfo::class.java).max()?.fileName
        }
        val seasonBanners = httpUtils.get(Constants.SERIES + "$id/images/query?keyType=season")
                .thenApply { jsonUtils.getListData(it, BannerInfo::class.java) }
                .thenApply { it.groupBy { it.key }.values.asSequence().map { it.max() }.filterNotNull().associateBy({ it.key!!.toInt() }, { it.fileName }) }

        val seasons = episodeService.getEpisodesOfSerial(id)
                .thenApply { it.groupBy { it.season }.entries.map { Season(number = it.key, episodes = it.value) } }
                .thenCombine(seasonBanners) { seasons, banners -> seasons.map { it.banner = banners[it.number]; return@map it } }

        return httpUtils.get(Constants.SERIES + id)
                .thenApply { jsonUtils.getSingleObject(it, Serial::class.java) }
                .thenCombine(poster) { serial, poster -> serial.poster = poster; return@thenCombine serial }
                .thenCombine(seasons) { serial, seasons -> serial.seasons = seasons; return@thenCombine serial }
    }

    override fun restoreAllData(): CompletableFuture<List<Serial>> = CompletableFuture.supplyAsync {
        serialRepository.getSeries(userService.getCurrentUser().id)
                .map { this.getSerial(it) }
                .map {
                    it.thenApply {
                        serial ->
                        episodeService.getWatchedEpisodes(serial.id!!)
                                .thenApply { setWatchedEpisodesToSerial(it, serial) }
                                .join()
                    }
                }
                .map { it.join() }
    }

    private fun setWatchedEpisodesToSerial(watchedEpisodes: List<Long>, serial: Serial): Serial {
        serial.seasons!!
                .asSequence()
                .flatMap { it.episodes!!.asSequence() }
                .filter { watchedEpisodes.contains(it.id) }
                .forEach { it.watched = true }
        return serial
    }

    override fun count(): Long = serialRepository.count(userService.getCurrentUser().id)

    override fun addSerial(id: Long) = serialRepository.add(userService.getCurrentUser().id, id)

    override fun deleteSerial(id: Long) = serialRepository.delete(userService.getCurrentUser().id, id)
}