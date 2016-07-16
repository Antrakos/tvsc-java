package com.tvsc.core.exception;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
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

    @SneakyThrows
    public <T> T wrap(Callable<T> statement) {
        try {
            return statement.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            Constructor<? extends ApplicationException> constructor = this.getClass().getConstructor(String.class, Throwable.class);
            throw constructor.newInstance(this.getMessage(), e);
        }
    }
}
