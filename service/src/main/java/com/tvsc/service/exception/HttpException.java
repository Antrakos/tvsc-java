package com.tvsc.service.exception;

import com.tvsc.core.exception.ApplicationException;

/**
 * @author Taras Zubrei
 */
public class HttpException extends ApplicationException {

    public HttpException(String url) {
        super("Cannot execute request: " + url);
    }
}
