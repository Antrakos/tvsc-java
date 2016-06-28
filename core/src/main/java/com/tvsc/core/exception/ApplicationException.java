package com.tvsc.core.exception;

/**
 * @author Taras Zubrei
 */
public abstract class ApplicationException extends RuntimeException {

    private Throwable cause;

    public ApplicationException(String message) {
        super(message);
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public abstract void afterThrowing();

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }

    @Override
    public String toString() {
        return "ApplicationException{ " +
                "exception=" + super.toString() +
                ", cause=" + cause +
                '}';
    }
}
