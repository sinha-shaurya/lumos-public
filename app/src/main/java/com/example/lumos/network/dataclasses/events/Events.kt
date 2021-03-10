package com.example.lumos.network.dataclasses.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Events(
    @Json(name="name") val eventName:String,
    @Json(name="description") val description:String,
    @Json(name = "is_completed") val isCompleted:Boolean,
    @Json(name="registration_link") val registrationLink:String?=null,
    @Json(name="event_date_set") val eventDates:List<EventDates>?=null
)