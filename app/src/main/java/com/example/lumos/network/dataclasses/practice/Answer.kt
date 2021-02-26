package com.example.lumos.network.dataclasses.practice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Answer(
    @Json(name = "answer") val answer: String,
    @Json(name = "pk") val primaryKey: Int
)
