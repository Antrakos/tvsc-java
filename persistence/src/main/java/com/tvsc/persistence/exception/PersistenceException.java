package com.tvsc.persistence.exception;

import com.tvsc.core.exception.ApplicationException;

/**
 * @author Taras Zubrei
 */
public class PersistenceException extends ApplicationException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
