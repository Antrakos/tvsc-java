package com.tvsc.core.immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.moshi.Json;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    String key();

    String fileName();

    RatingsInfo ratingsInfo();

    default int compareTo(BannerInfo o) {
        return this.ratingsInfo().average.compareTo(o.ratingsInfo().average);
    }

    class RatingsInfo {
        Double average;
        public Long count;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public RatingsInfo(@JsonProperty("average") Double average,
                           @JsonProperty("count") Long count) {
            this.average = average;
            this.count = count;
        }
    }

    class Builder extends ImmutableBannerInfo.Builder {
    }
}
