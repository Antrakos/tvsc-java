package com.tvsc.core.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

/**
 * @author Taras Zubrei
 */
data class Episode (
        @JsonProperty                       var id: Long? = null,
        @JsonProperty("episodeName")        var name: String? = null,
        @JsonProperty("airedEpisodeNumber") var number: Int? = null,
        @JsonProperty                       var firstAired: LocalDate? = null,
        @JsonProperty                       var overview: String? = null,
        @JsonProperty("siteRating")         var rating: Double? = null,
        @JsonProperty                       var imdbId: String? = null,
        @JsonProperty("airedSeason")        var season: Int? = null,
                                            var watched: Boolean = false,
        @JsonProperty("filename")           var image: String? = null
        )
