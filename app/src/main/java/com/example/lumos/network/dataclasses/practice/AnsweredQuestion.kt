package com.example.lumos.network.dataclasses.practice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnsweredQuestion(
    @Json(name="question") val question: Question,
    @Json(name="answer") val answerSubmit:String,
    @Json(name="points") val points:Int?
)
