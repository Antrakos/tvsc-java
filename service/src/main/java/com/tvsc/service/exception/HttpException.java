package com.tvsc.service.exception;

import com.tvsc.core.exception.ApplicationException;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author Taras Zubrei
 */
public class HttpException extends ApplicationException {
    private HttpRequestBase request;

    public HttpException(HttpRequestBase request) {
        super("Cannot execute request: "+request);
        this.request = request;
    }

    @Override
    public void afterThrowing() {
        request.releaseConnection();
    }
}
