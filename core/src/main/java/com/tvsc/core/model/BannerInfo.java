package com.tvsc.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author Taras Zubrei
 */
@Data
@NoArgsConstructor
public class BannerInfo implements Comparable<BannerInfo>{
    @JsonProperty("keyType")
    private String type;
    @JsonProperty("subKey")
    private String key;
    @JsonProperty
    private String fileName;
    @JsonProperty
    private RatingsInfo ratingsInfo;

    @Override
    public int compareTo(BannerInfo o) {
        return this.ratingsInfo.average.compareTo(o.ratingsInfo.average);
    }

    @Data
    @NoArgsConstructor
    public static class RatingsInfo {
        @JsonProperty
        private Double average;
        @JsonProperty
        private Long count;
    }
}
