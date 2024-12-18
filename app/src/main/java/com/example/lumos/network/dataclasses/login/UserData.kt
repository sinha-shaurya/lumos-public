package com.example.lumos.network.dataclasses.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserData(
    @Json(name = "status") val status: String,
    @Json(name = "token") val token: String? = null,
    @Json(name = "username") val userName: String? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "first_name") val firstName: String? = null,
    @Json(name = "last_name") val lastName: String? = null
)