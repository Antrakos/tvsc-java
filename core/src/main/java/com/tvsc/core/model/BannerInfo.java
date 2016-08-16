package com.tvsc.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.squareup.moshi.Json;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Taras Zubrei
 */
@Data
@NoArgsConstructor
public class BannerInfo implements Comparable<BannerInfo> {
    @JsonProperty("keyType")
    @Json(name = "keyType")
    private String type;
    @JsonProperty("subKey")
    @Json(name = "subKey")
    private String key;
    private String fileName;
    private RatingsInfo ratingsInfo;

    @Override
    public int compareTo(BannerInfo o) {
        return this.ratingsInfo.average.compareTo(o.ratingsInfo.average);
    }

    @Data
    @NoArgsConstructor
    public static class RatingsInfo {
        private Double average;
        private Long count;
    }
}
