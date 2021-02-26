package com.example.lumos.network.dataclasses.practice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnswerResponse(
    @Json(name="status") val status:String="unsuccessful",
    @Json(name="errors") val errors: ResponseErrors?=null,
)