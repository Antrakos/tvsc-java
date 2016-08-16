package com.tvsc.core.immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.moshi.Json;
import org.immutables.value.Value.Default;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

import static org.immutables.value.Value.Immutable;

/**
 * @author Taras Zubrei
 */
@Immutable
@JsonSerialize(as = ImmutableEpisode.class)
@JsonDeserialize(as = ImmutableEpisode.class)
public interface Episode extends WithEpisode {
    Long id();

    @JsonProperty("episodeName")
    @Json(name = "episodeName")
    @Nullable String name();

    @JsonProperty("airedEpisodeNumber")
    @Json(name = "airedEpisodeNumber")
    @Nullable Integer number();

    @Nullable LocalDate firstAired();

    @Nullable String overview();

    @JsonProperty("siteRating")
    @Json(name = "siteRating")
    @Nullable Double rating();

    @Nullable String imdbId();

    @JsonProperty("airedSeason")
    @Json(name = "airedSeason")
    @Nullable Integer season();

    @Default
    default Boolean watched() {
        return false;
    }

    @JsonProperty("filename")
    @Json(name = "filename")
    @Nullable String image();

    class Builder extends ImmutableEpisode.Builder {
    }
}
