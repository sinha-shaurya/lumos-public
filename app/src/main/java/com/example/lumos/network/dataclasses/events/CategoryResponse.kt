package com.example.lumos.network.dataclasses.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponse(
    @Json(name = "status") val status: String,
    @Json(name = "active") val activeCategory: List<Category>
)