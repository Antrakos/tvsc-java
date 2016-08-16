package com.tvsc.service.json

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

/**
 *
 * @author Taras Zubrei
 */
class LocalDateAdapter {
    @ToJson fun toJson(localDate: LocalDate): String = localDate.toString()
    @FromJson fun fromJson(localDate: String): LocalDate = LocalDate.parse(localDate)
}