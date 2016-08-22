package com.tvsc.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.moshi.Json;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * @author Taras Zubrei
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Episode {
    private Long id;
    @JsonProperty("episodeName")
    @Json(name = "episodeName")
    private String name;
    @JsonProperty("airedEpisodeNumber")
    @Json(name = "airedEpisodeNumber")
    private Integer number;
    private LocalDate firstAired;
    private String overview;
    @JsonProperty("siteRating")
    @Json(name = "siteRating")
    private Double rating;
    private String imdbId;
    @JsonProperty("airedSeason")
    @Json(name = "airedSeason")
    private Integer season;
    private Boolean watched = false;
    @JsonProperty("filename")
    @Json(name = "filename")
    private String image;
}
