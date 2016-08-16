package com.tvsc.core.immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.moshi.Json;

import static org.immutables.value.Value.Default;
import static org.immutables.value.Value.Immutable;

/**
 * @author Taras Zubrei
 */
@Immutable
@JsonSerialize(as = ImmutableBannerInfo.class)
@JsonDeserialize(as = ImmutableBannerInfo.class)
public interface BannerInfo extends WithBannerInfo, Comparable<BannerInfo> {
    @JsonProperty("keyType")
    @Json(name = "keyType")
    String type();

    @JsonProperty("subKey")
    @Json(name = "subKey")
    @Default
    default String key() {
        return null;
    }

    String fileName();

    RatingsInfo ratingsInfo();

    default int compareTo(BannerInfo o) {
        return this.ratingsInfo().average().compareTo(o.ratingsInfo().average());
    }

    @Immutable
    interface RatingsInfo extends WithRatingsInfo {
        Double average();

        Long count();

        class Builder extends ImmutableRatingsInfo.Builder {
        }
    }

    class Builder extends ImmutableBannerInfo.Builder {
    }
}
