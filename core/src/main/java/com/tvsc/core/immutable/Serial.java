package com.tvsc.core.immutable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.squareup.moshi.Json;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.jetbrains.annotations.Nullable;

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
    @Nullable String name();

    @Nullable String banner();

    @Nullable String overview();

    @Nullable LocalDate firstAired();

    @Nullable String network();

    @Nullable String imdbId();

    @Nullable String zap2itId();

    @Nullable String airsDayOfWeek();

    @Nullable String airsTime();

    @Default
    default List<String> genre() {
        return null;
    }

    @Nullable String rating();

    @Nullable String runtime();

    @Nullable String status();

    @Nullable String poster();

    @Nullable
    List<Season> seasons();

    class Builder extends ImmutableSerial.Builder {
    }
}
