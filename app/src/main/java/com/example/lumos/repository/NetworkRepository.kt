package com.example.lumos.repository

import com.example.lumos.local.LocalUser
import com.example.lumos.local.UserDao
import com.example.lumos.network.IsteNetworkInstance
import com.example.lumos.network.dataclasses.RegistrationResponseData
import com.example.lumos.network.dataclasses.login.LoginUserData
import com.example.lumos.network.dataclasses.practice.Answer
import com.example.lumos.network.dataclasses.registration.RegistrationData

class NetworkRepository(private val userDao: UserDao) {
    suspend fun loginUser(loginUserData: LoginUserData) =
        IsteNetworkInstance.api.loginUser(loginUserData)

    suspend fun registerUser(registrationData: RegistrationData): RegistrationResponseData =
        IsteNetworkInstance.api.registerUser(registrationData)

    suspend fun getUserDataCount() = userDao.getUserDataCount()

    suspend fun getUserData() = userDao.getUserData()

    suspend fun logoutUser(localUser: LocalUser) = userDao.logoutUser(localUser)

    suspend fun addUser(localUser: LocalUser) = userDao.addUser(localUser)

    //get category
    suspend fun getCategory() = IsteNetworkInstance.api.getCategory()

    suspend fun getAuthToken() = userDao.getAuthToken()

    suspend fun getQuestions(headers: MutableMap<String, String?>) =
        IsteNetworkInstance.api.getQuestions(headers)

    suspend fun submitAnswer(headers: MutableMap<String, String>, answer: Answer) =
        IsteNetworkInstance.api.submitAnswer(headers,answer = answer)

    //suspend fun submitAnswer(headers: MutableMap<String, String>, answer: Answer) =IsteNetworkInstance.api.submitAnswer(headers,answer.answer,answer.primaryKey)

    suspend fun getSubmittedAnswer(headers:Map<String,String>)=IsteNetworkInstance.api.getSubmittedAnswer(headers)

    suspend fun getEvents(categoryNameSlug:String) =IsteNetworkInstance.api.getEvent(categoryNameSlug)
}