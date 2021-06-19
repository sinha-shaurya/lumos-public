package com.example.lumos.utils.developers


//data class to hold the details of each developer
data class Developer(
    val name: String,
    val role: String,
    val gitHub: String = "",
    val linkedIn: String = "",
    val instagram: String = "",
    val personalWebsite: String = "",
    val twitter: String = "",
    val email: String = ""
)