package com.example.lumos.network.dataclasses.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventResponse(
    @Json(name = "status") val status: String,
    @Json(name = "events") val eventList: List<Events>
)