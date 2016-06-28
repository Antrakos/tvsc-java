package com.tvsc.core.exception;

/**
 * This code may throw a {@code Throwable}. Therefore we cannot use
 * {@code Callable}.
 */
@FunctionalInterface
public interface StatementWithReturnValue<V> {
    /**
     * Computes a value, or throws an exception if unable to do so.
     *
     * @return computed value
     * @throws Throwable an exception if it cannot compute the value
     */
    V evaluate() throws Throwable;
}