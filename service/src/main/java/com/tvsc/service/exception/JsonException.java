package com.tvsc.service.exception;

import com.tvsc.core.exception.ApplicationException;

/**
 * @author Taras Zubrei
 */
public class JsonException extends ApplicationException {
    public JsonException(String message) {
        super(message);
    }
}
