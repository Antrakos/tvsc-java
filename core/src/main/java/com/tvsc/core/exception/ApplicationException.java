package com.tvsc.core.exception;

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

    @Override
    public String toString() {
        return "ApplicationException{ " +
                "exception=" + super.toString() +
                ", cause=" + super.getCause() +
                '}';
    }
}
