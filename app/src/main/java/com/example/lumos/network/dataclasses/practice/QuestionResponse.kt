package com.example.lumos.network.dataclasses.practice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionResponse(
    @Json(name = "questions") val questionList: MutableList<Question>? = null,
    @Json(name = "status") val questionStatus: String = "unsuccessful",
    @Json(name = "detail") val errorDetails: String? = null
)