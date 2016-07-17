package com.tvsc.core.exception;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author Taras Zubrei
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> T wrap(Callable<T> statement) {
        try {
            return statement.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            try {
                this.getClass().getMethod("initCause", Throwable.class).invoke(this, e);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                throw new RuntimeException("Failed to set cause to Exception instance", ex);
            }
            throw this;
        }
    }
}
