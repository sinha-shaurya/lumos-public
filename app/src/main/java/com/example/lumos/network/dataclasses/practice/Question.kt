package com.example.lumos.network.dataclasses.practice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question(
    @Json(name = "pk") val primaryKey: Int,
    @Json(name = "mentor") val mentor: MentorDetails,
    @Json(name = "question") val question: String,
    @Json(name = "category") val category: String? = null,
    @Json(name = "expected_answer") val expectedAnswer: String
)