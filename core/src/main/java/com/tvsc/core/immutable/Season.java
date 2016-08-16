package com.tvsc.core.immutable;

import org.immutables.value.Value.Immutable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Immutable
public interface Season extends WithSeason {
    Integer number();

    @Nullable String banner();

    List<Episode> episodes();

    class Builder extends ImmutableSeason.Builder {
    }
}
