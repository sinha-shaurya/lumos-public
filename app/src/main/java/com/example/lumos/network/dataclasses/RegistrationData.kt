package com.example.lumos.network.dataclasses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationData(
    @Json(name = "username") val username: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "password2") val passwordConfirm: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String
)