package com.tvsc.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Taras Zubrei
 */
@Data
@NoArgsConstructor
public class Serial {
    @JsonProperty
    private Long id;
    @JsonProperty("seriesName")
    private String name;
    @JsonProperty
    private String banner;
    @JsonProperty
    private String overview;
    @JsonProperty
    private LocalDate firstAired;
    @JsonProperty
    private String network;
    @JsonProperty
    private String imdbId;
    @JsonProperty
    private String zap2itId;
    @JsonProperty
    private String airsDayOfWeek;
    @JsonProperty
    private String airsTime;
    @JsonProperty
    private List<String> genre;
    @JsonProperty
    private String rating;
    @JsonProperty
    private String runtime;
    @JsonProperty
    private String status;
    private String poster;
    private List<Season> seasons;
}
