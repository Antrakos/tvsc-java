package com.tvsc.core.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Taras Zubrei
 */
data class BannerInfo(
        @JsonProperty("keyType") var type: String? = null,
        @JsonProperty("subKey") var key: String? = null,
        @JsonProperty var fileName: String? = null,
        @JsonProperty var ratingsInfo: RatingsInfo? = null
) : Comparable<BannerInfo> {
    override fun compareTo(other: BannerInfo): Int {
        return this.ratingsInfo?.average!!.compareTo(other.ratingsInfo!!.average);
    }

};
data class RatingsInfo(
        @JsonProperty var average: Double,
        @JsonProperty var count: Long
)