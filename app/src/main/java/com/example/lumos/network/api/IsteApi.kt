package com.example.lumos.network.api

import com.example.lumos.network.dataclasses.RegistrationResponseData
import com.example.lumos.network.dataclasses.events.Category
import com.example.lumos.network.dataclasses.events.CategoryResponse
import com.example.lumos.network.dataclasses.login.LoginUserData
import com.example.lumos.network.dataclasses.login.UserData
import com.example.lumos.network.dataclasses.registration.RegistrationData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IsteApi {
    @POST("api/login")
    suspend fun loginUser(@Body loginUserData: LoginUserData): UserData

    @POST("api/register")
    suspend fun registerUser(@Body registrationData: RegistrationData): RegistrationResponseData

    @GET("api/category")
    suspend fun getCategory(): CategoryResponse
}