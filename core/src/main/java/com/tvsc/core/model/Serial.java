package com.tvsc.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.moshi.Json;
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
    private Long id;
    @JsonProperty("seriesName")
    @Json(name = "seriesName")
    private String name;
    private String banner;
    private String overview;
    private LocalDate firstAired;
    private String network;
    private String imdbId;
    private String zap2itId;
    private String airsDayOfWeek;
    private String airsTime;
    private List<String> genre;
    private String rating;
    private String runtime;
    private String status;
    private String poster;
    private List<Season> seasons;
}
