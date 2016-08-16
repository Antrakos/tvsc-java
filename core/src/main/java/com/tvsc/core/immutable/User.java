package com.tvsc.core.immutable;

import org.immutables.value.Value.Immutable;

/**
 * @author Taras Zubrei
 */
@Immutable
public interface User extends WithUser {
    Long id();

    String name();

    class Builder extends ImmutableUser.Builder {
    }
}
