package com.tvsc.core.model

import com.fasterxml.jackson.annotation.JsonProperty

import java.time.LocalDate

/**
 * @author Taras Zubrei
 */
data class Serial (
        @JsonProperty                   var id: Long? = null,
        @JsonProperty("seriesName")     var name: String? = null,
        @JsonProperty                   var banner: String? = null,
        @JsonProperty                   var overview: String? = null,
        @JsonProperty                   var firstAired: LocalDate? = null,
        @JsonProperty                   var network: String? = null,
        @JsonProperty                   var imdbId: String? = null,
        @JsonProperty                   var zap2itId: String? = null,
        @JsonProperty                   var airsDayOfWeek: String? = null,
        @JsonProperty                   var airsTime: String? = null,
        @JsonProperty                   var genre: List<String>? = null,
        @JsonProperty                   var rating: String? = null,
        @JsonProperty                   var runtime: String? = null,
        @JsonProperty                   var status: String? = null,
                                        var poster: String? = null,
                                        var seasons: List<Season>? = null
    //    String actors;
    //    String contentRating;

)
