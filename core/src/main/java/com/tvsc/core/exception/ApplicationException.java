package com.tvsc.core.exception;

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
        return ExceptionUtil.wrapCheckedException(this, statement);
    }
}
