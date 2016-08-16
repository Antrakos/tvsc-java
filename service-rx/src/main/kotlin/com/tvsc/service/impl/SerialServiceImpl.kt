package com.tvsc.service.impl

import com.tvsc.core.immutable.BannerInfo
import com.tvsc.core.immutable.Season
import com.tvsc.core.immutable.Serial
import com.tvsc.persistence.repository.SerialRepository
import com.tvsc.service.Constants
import com.tvsc.service.EpisodeService
import com.tvsc.service.SerialService
import com.tvsc.service.UserService
import com.tvsc.service.util.HttpUtils
import com.tvsc.service.util.JsonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import rx.Observable
import rx.Single

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

    override fun getSerialInfo(id: Long): Single<Serial> = httpUtils.get(Constants.SERIES + id).map { jsonUtils.getSingleObject(it, Serial::class.java) }

    override fun getSerial(id: Long): Single<Serial> {

        val poster = httpUtils.get(Constants.SERIES + "$id/images/query?keyType=poster").map {
            jsonUtils.getListData(it, BannerInfo::class.java).max()?.fileName()
        }
        val seasonBanners: Observable<String> = httpUtils.get(Constants.SERIES + "$id/images/query?keyType=season")
                .map { jsonUtils.getListData(it, BannerInfo::class.java) }
                .flatMapObservable { Observable.from(it) }
                .groupBy { it.key() }
                .flatMap { it.toList().map { it.max() }.map { it!!.fileName() } }

        val seasons: Observable<Season> = episodeService.getEpisodesOfSerial(id)
                .groupBy { it.season() }
                .flatMap { group -> group.toList().map { Season.Builder().number(group.key).episodes(it).build() } }
                .zipWith(seasonBanners) { season, banner -> season.withBanner(banner) }

        return httpUtils.get(Constants.SERIES + id)
                .map { jsonUtils.getSingleObject(it, Serial::class.java) }
                .flatMap { serial -> poster.map { serial.withPoster(it) } }
                .flatMap { serial -> seasons.toList().toSingle().map { serial.withSeasons(it) } }
    }

    override fun restoreAllData(): Observable<Serial> = Observable.just(serialRepository.getSeries(userService.getCurrentUser().id()))
            .flatMapIterable { it }
            .flatMap { this.getSerial(it).toObservable() }
            .flatMap {
                serial ->
                episodeService.getWatchedEpisodes(serial.id())
                        .toList()
                        .map { setWatchedEpisodesToSerial(it, serial) }
            }

    private fun setWatchedEpisodesToSerial(watchedEpisodes: List<Long>, serial: Serial): Serial =
            serial.withSeasons(
                    serial.seasons()
                            !!.map {
                                it.withEpisodes(it.episodes()
                                        .asSequence()
                                        .filter { watchedEpisodes.contains(it.id()) }
                                        .map { it.withWatched(true) }
                                        .toList())
                            }
            )


    override fun count(): Long = serialRepository.count(userService.getCurrentUser().id())

    override fun addSerial(id: Long) = serialRepository.add(userService.getCurrentUser().id(), id)

    override fun deleteSerial(id: Long) = serialRepository.delete(userService.getCurrentUser().id(), id)
}