package com.example.lumos.network.dataclasses.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventDates(
    @Json(name = "venue") val venue: String?,
    @Json(name = "start_date") val startDate: String?,
    @Json(name = "end_date") val endDate: String?
)