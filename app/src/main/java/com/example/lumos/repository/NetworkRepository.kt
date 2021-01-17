package com.example.lumos.repository

import com.example.lumos.network.IsteNetworkInstance
import com.example.lumos.network.dataclasses.LoginUserData

class NetworkRepository {
    suspend fun loginUser(loginUserData: LoginUserData) =
        IsteNetworkInstance.api.loginUser(loginUserData)
}