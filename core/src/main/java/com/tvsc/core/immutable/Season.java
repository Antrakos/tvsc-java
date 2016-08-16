package com.tvsc.core.immutable;

import com.tvsc.core.model.Episode;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import java.util.List;

/**
 * @author Taras Zubrei
 */
@Immutable
public interface Season extends WithSeason {
    Integer number();

    @Default
    default String banner() {
        return null;
    }

    List<Episode> episodes();

    class Builder extends ImmutableSeason.Builder {
    }
}
