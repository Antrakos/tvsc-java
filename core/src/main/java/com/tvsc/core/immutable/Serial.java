package com.tvsc.core.immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.moshi.Json;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Taras Zubrei
 */
@Immutable
@JsonSerialize(as = ImmutableSerial.class)
@JsonDeserialize(as = ImmutableSerial.class)
public interface Serial extends WithSerial {
    Long id();

    @JsonProperty("seriesName")
    @Json(name = "seriesName")
    @Default
    default String name() {
        return null;
    }

    @Default
    default String banner() {
        return null;
    }

    @Default
    default String overview() {
        return null;
    }

    @Default
    default LocalDate firstAired() {
        return null;
    }

    @Default
    default String network() {
        return null;
    }

    @Default
    default String imdbId() {
        return null;
    }

    @Default
    default String zap2itId() {
        return null;
    }

    @Default
    default String airsDayOfWeek() {
        return null;
    }

    @Default
    default String airsTime() {
        return null;
    }

    @Default
    default List<String> genre() {
        return null;
    }

    @Default
    default String rating() {
        return null;
    }

    @Default
    default String runtime() {
        return null;
    }

    @Default
    default String status() {
        return null;
    }

    @Default
    default String poster() {
        return null;
    }

    @Default
    default List<Season> seasons() {
        return null;
    }

    class Builder extends ImmutableSerial.Builder {
    }
}
