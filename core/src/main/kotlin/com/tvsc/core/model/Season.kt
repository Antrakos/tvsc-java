package com.tvsc.core.model

/**
 * @author Taras Zubrei
 */
data class Season(
        var number: Int? = null,
        var banner: String? = null,
        var episodes: List<Episode>? = null
)
