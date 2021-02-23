package com.example.lumos.network.dataclasses.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "name") val name: String,
    @Json(name = "start_date") val startDate: String?,
    @Json(name = "end_date") val endDate: String?,
    @Json(name = "poster") val posterSlug: String?,
    @Json(name = "name_slug") val nameSlug: String,
    @Json(name = "description") val description: String? = null,
    @Json(name = "registration_link") val registrationLink: String? = null,
    @Json(name = "events") val events: List<Events>
)
