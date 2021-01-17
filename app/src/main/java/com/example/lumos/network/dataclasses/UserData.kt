package com.example.lumos.network.dataclasses

import com.squareup.moshi.Json

data class UserData(
    @Json(name = "status") val status: String,
    @Json(name = "token") val token: String? = null,
    @Json(name = "username") val userName: String? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "first_name") val firstName: String? = null,
    @Json(name = "last_name") val lastName: String? = null
)