package com.tvsc.core.immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.moshi.Json;

import java.time.LocalDate;

import static org.immutables.value.Value.Default;
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
    @Default
    default String name() {
        return null;
    }

    @JsonProperty("airedEpisodeNumber")
    @Json(name = "airedEpisodeNumber")
    @Default
    default Integer number() {
        return null;
    }

    @Default
    default LocalDate firstAired() {
        return null;
    }

    @Default
    default String overview() {
        return null;
    }

    @JsonProperty("siteRating")
    @Json(name = "siteRating")
    @Default
    default Double rating() {
        return null;
    }

    @Default
    default String imdbId() {
        return null;
    }

    @JsonProperty("airedSeason")
    @Json(name = "airedSeason")
    @Default
    default Integer season() {
        return null;
    }

    @Default
    default Boolean watched() {
        return false;
    }

    @JsonProperty("filename")
    @Json(name = "filename")
    @Default
    default String image() {
        return null;
    }

    class Builder extends ImmutableEpisode.Builder {
    }
}
