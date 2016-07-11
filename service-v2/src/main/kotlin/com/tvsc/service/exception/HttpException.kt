package com.tvsc.service.exception

import com.tvsc.core.exception.ApplicationException

/**
 *
 * @author Taras Zubrei
 */
class HttpException : ApplicationException {
    constructor(url: String) : super("Cannot execute request: $url")
}