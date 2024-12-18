package com.example.lumos.network.dataclasses.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginUserData(
    val username: String,
    val password: String
)