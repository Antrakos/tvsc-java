package com.tvsc.service.json

/**
 *
 * @author Taras Zubrei
 */
data class Links(var first: Int = 1, var last: Int = 1, var next: Int? = null, var previous: Int? = null)
data class Page<T> (var data: T? = null, var links: Links? = null)