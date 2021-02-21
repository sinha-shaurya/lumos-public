package com.example.lumos.network.dataclasses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationResponseData(
    @Json(name = "status") val registrationStatus: String,
    @Json(name = "details") val registrationDetails: RegistrationDetails? = null,
    @Json(name = "errors") val errors: Errors? = null
)

@JsonClass(generateAdapter = true)
data class RegistrationDetails(
    @Json(name = "username") val username: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "email") val email: String,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class Errors(
    @Json(name = "username") val username: List<String>? = null,
    @Json(name = "password") val password: List<String>? = null
)