package com.example.lumos.repository

import com.example.lumos.local.LocalUser
import com.example.lumos.local.UserDao
import com.example.lumos.network.IsteNetworkInstance
import com.example.lumos.network.dataclasses.LoginUserData
import com.example.lumos.network.dataclasses.RegistrationData
import com.example.lumos.network.dataclasses.RegistrationResponseData

class NetworkRepository(private val userDao: UserDao) {
    suspend fun loginUser(loginUserData: LoginUserData) =
        IsteNetworkInstance.api.loginUser(loginUserData)

    suspend fun registerUser(registrationData: RegistrationData): RegistrationResponseData =
        IsteNetworkInstance.api.registerUser(registrationData)

    suspend fun getUserDataCount() = userDao.getUserDataCount()

    suspend fun getUserData() = userDao.getUserData()

    suspend fun logoutUser(localUser: LocalUser) = userDao.logoutUser(localUser)

    suspend fun addUser(localUser: LocalUser) = userDao.addUser(localUser)

}