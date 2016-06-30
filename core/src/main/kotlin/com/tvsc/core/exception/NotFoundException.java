package com.tvsc.core.exception;

/**
 * @author Taras Zubrei
 */
public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable exception) {
        super(message, exception);
    }
}
