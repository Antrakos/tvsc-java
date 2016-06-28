package com.tvsc.core.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Taras Zubrei
 */
data class BannerInfo(
        @JsonProperty("keyType") var type: String,
        @JsonProperty("subKey") var key: String? = null,
        @JsonProperty var fileName: String,
        @JsonProperty var ratingsInfo: RatingsInfo
) : Comparable<BannerInfo> {
    override fun compareTo(other: BannerInfo): Int {
        return this.ratingsInfo.average.compareTo(other.ratingsInfo.average);
    }

    data class RatingsInfo(
            @JsonProperty var average: Double,
            @JsonProperty var count: Long
    )
};
