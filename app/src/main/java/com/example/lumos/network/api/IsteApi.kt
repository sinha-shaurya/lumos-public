package com.example.lumos.network.api

import com.example.lumos.network.dataclasses.RegistrationResponseData
import com.example.lumos.network.dataclasses.events.CategoryResponse
import com.example.lumos.network.dataclasses.events.EventResponse
import com.example.lumos.network.dataclasses.login.LoginUserData
import com.example.lumos.network.dataclasses.login.UserData
import com.example.lumos.network.dataclasses.practice.Answer
import com.example.lumos.network.dataclasses.practice.AnswerResponse
import com.example.lumos.network.dataclasses.practice.AnsweredQuestionResponse
import com.example.lumos.network.dataclasses.practice.QuestionResponse
import com.example.lumos.network.dataclasses.registration.RegistrationData
import retrofit2.http.*

interface IsteApi {
    @POST("api/login")

    suspend fun loginUser(@Body loginUserData: LoginUserData): UserData

    @POST("api/register")
    suspend fun registerUser(@Body registrationData: RegistrationData): RegistrationResponseData

    @GET("api/category")
    suspend fun getCategory(): CategoryResponse

    @GET("api/interview/questions")
    suspend fun getQuestions(@HeaderMap headers: Map<String, String?>): QuestionResponse


    /*
    @POST("api/interview/submit")
    @FormUrlEncoded
    suspend fun submitAnswer(
        @HeaderMap headers: Map<String, String?>,
        @Field("answer") answer: String,
        @Field("pk") pk: Int
    ): AnswerResponse
     */


    @POST("api/interview/submit")
    suspend fun submitAnswer(
        @HeaderMap headers: Map<String, String>,
        @Body answer: Answer
    ): AnswerResponse

    @GET("api/interview/submitted")
    suspend fun getSubmittedAnswer(@HeaderMap header: Map<String, String>): AnsweredQuestionResponse

    @GET("api/event/{category}")
    suspend fun getEvent(@Path("category") categoryNameSlug: String):EventResponse

}