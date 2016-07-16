package com.tvsc.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Taras Zubrei
 */
@Data
@NoArgsConstructor
public class Episode {
    @JsonProperty
    private Long id;
    @JsonProperty("episodeName")
    private String name;
    @JsonProperty("airedEpisodeNumber")
    private Integer number;
    @JsonProperty
    private LocalDate firstAired;
    @JsonProperty
    private String overview;
    @JsonProperty("siteRating")
    private Double rating;
    @JsonProperty
    private String imdbId;
    @JsonProperty("airedSeason")
    private Integer season;
    private Boolean watched;
    @JsonProperty("filename")
    private String image;
}
