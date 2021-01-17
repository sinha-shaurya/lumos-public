package com.example.lumos.network.api

import com.example.lumos.network.dataclasses.LoginUserData
import com.example.lumos.network.dataclasses.UserData
import retrofit2.http.Body
import retrofit2.http.POST

interface IsteApi {
    @POST("api/login")
    suspend fun loginUser(@Body loginUserData: LoginUserData):UserData
}