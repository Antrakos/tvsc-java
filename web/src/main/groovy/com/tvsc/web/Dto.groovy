package com.tvsc.web

import groovy.transform.Canonical

import java.time.LocalDate

/**
 *
 * @author Taras Zubrei
 */
@Canonical
class EpisodeDto {
    Long id
    LocalDate firstAired
    String image
    String imdbId
    String name
    Integer number
    String overview
    Double rating
    Boolean watched
}

@Canonical
class SeasonDto {
    Integer number
    String banner
    List<EpisodeDto> episodes
}

@Canonical
class SerialDto {
    Long id
    String name
    String airsDayOfWeek
    String airsTime
    String banner
    LocalDate firstAired
    List<String> genre
    String imdbId
    String network
    String overview
    String poster
    String rating
    String runtime
    String status
    String zap2itId
    List<SeasonDto> seasons
}