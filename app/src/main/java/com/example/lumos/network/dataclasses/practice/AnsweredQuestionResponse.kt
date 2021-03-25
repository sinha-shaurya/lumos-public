package com.example.lumos.network.dataclasses.practice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnsweredQuestionResponse(
    @Json(name="status") val status:String,
    @Json(name="answers") val answeredQuestionsList:List<AnsweredQuestion>?= emptyList(),
)