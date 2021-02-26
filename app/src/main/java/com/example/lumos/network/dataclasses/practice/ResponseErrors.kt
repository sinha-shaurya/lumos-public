package com.example.lumos.network.dataclasses.practice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseErrors(
    @Json(name="pk") val primaryKeyError:List<String>?=null
)