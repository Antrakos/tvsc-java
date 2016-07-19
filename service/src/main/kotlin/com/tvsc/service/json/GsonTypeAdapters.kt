package com.tvsc.service.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tvsc.core.model.BannerInfo
import com.tvsc.core.model.Episode
import com.tvsc.core.model.Serial
import java.lang.reflect.Type
import java.time.LocalDate

/**
 *
 * @author Taras Zubrei
 */
class EpisodeDeserializer : JsonDeserializer<Episode> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): Episode {
        val episode = Episode()
        val jsonObject = json.asJsonObject
        episode.id = jsonObject.get("id").asLong
        episode.firstAired = context.deserialize(jsonObject.get("firstAired"), LocalDate::class.java)
        episode.image = jsonObject.get("filename")?.asString
        episode.imdbId = jsonObject.get("imdbId")?.asString
        episode.season = jsonObject.get("airedSeason")?.asInt
        episode.rating = jsonObject.get("siteRating")?.asDouble
        episode.overview = jsonObject.get("overview")?.asString
        episode.number = jsonObject.get("airedEpisodeNumber")?.asInt
        episode.name = jsonObject.get("episodeName")?.asString
        return episode
    }
}

class BannerInfoDeserializer : JsonDeserializer<BannerInfo> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): BannerInfo {
        val bannerInfo = BannerInfo()
        val jsonObject = json.asJsonObject
        bannerInfo.type = jsonObject.get("keyType").asString
        bannerInfo.key = jsonObject.get("subKey").asString
        bannerInfo.fileName = jsonObject.get("fileName").asString
        val ratingsInfo = BannerInfo.RatingsInfo()
        val ratingsInfoJson = jsonObject.get("ratingsInfo").asJsonObject
        ratingsInfo.average = ratingsInfoJson.get("average").asDouble
        ratingsInfo.count = ratingsInfoJson.get("count").asLong
        bannerInfo.ratingsInfo = ratingsInfo
        return bannerInfo
    }
}

class SerialDeserializer : JsonDeserializer<Serial> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): Serial {
        val serial = Serial()
        val jsonObject = json.asJsonObject
        serial.id = jsonObject.get("id").asLong
        serial.name = jsonObject.get("seriesName").asString
        serial.banner = jsonObject.get("banner").asString
        serial.overview = jsonObject.get("overview").asString
        serial.firstAired = context.deserialize(jsonObject.get("firstAired"), LocalDate::class.java)
        serial.network = jsonObject.get("network").asString
        serial.imdbId = jsonObject.get("imdbId").asString
        serial.zap2itId = jsonObject.get("zap2itId").asString
        serial.airsDayOfWeek = jsonObject.get("airsDayOfWeek").asString
        serial.airsTime = jsonObject.get("airsTime").asString
        serial.genre = jsonObject.get("genre").asJsonArray.map { it.asString }
        serial.rating = jsonObject.get("rating").asString
        serial.runtime = jsonObject.get("runtime").asString
        serial.status = jsonObject.get("status").asString
        return serial
    }
}

class LocalDateDeserializer : JsonDeserializer<LocalDate> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate? {
        val firstAired = json.asString
        return if (firstAired != null) LocalDate.parse(firstAired) else null
    }

}