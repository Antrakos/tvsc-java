package com.tvsc.service.exception

import com.tvsc.core.exception.ApplicationException

/**
 *
 * @author Taras Zubrei
 */
class NotFoundException : ApplicationException {
    constructor(message: String) : super("Cannot find results for given parameters: $message")
}