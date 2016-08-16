package com.tvsc.service.exception

import com.tvsc.core.exception.ApplicationException

/**
 *
 * @author Taras Zubrei
 */
class HttpException : ApplicationException {
    constructor(url: String) : super("Cannot execute request: $url")
    constructor(url:String, cause: Throwable) : super("Cannot execute request: $url", cause)
}