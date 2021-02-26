package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.local.LocalUser
import com.example.lumos.network.dataclasses.login.LoginUserData
import com.example.lumos.network.dataclasses.login.UserData
import com.example.lumos.network.dataclasses.practice.QuestionResponse
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoginStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: NetworkRepository) : ViewModel() {
    val loginState = MutableLiveData<Boolean>()
    val userCurrent = MutableLiveData<UserData>()
    val loginLevel = MutableLiveData<Int>()
    val localData = MutableLiveData<UserData>()
    val localUserLoggedIn = MutableLiveData<LocalUser>()
    val loginStatus = MutableLiveData<LoginStatus>()
    val questionResponse = MutableLiveData<QuestionResponse>()

    init {
        Log.i(TAG, "LoginViewModel created")
        userCurrent.value = null
        loginLevel.value = 0
        localData.value = UserData(status = "unsuccessful")
        localUserLoggedIn.value = null
        loginStatus.value = LoginStatus.LOADING
        questionResponse.value = null
        getUserDataCount()
    }

    fun changeState() {
        loginState.value = !loginState.value!!
    }

    fun loginUser(username: String, password: String) {
        val loginUserData = LoginUserData(username, password)
        viewModelScope.launch(Dispatchers.IO) {
            val receivedUser = repository.loginUser(loginUserData)
            if (receivedUser.status.equals("successful")) {
                //change login state
                loginState.postValue(true)
                userCurrent.postValue(receivedUser)
                loginLevel.postValue(1)
                loginStatus.postValue(LoginStatus.SUCCESS)
                //also trigger database call or preference datastore storage
                val localUser = LocalUser(
                    0,
                    receivedUser.status,
                    receivedUser.token!!,
                    receivedUser.userName!!,
                    receivedUser.email!!,
                    receivedUser.firstName!!,
                    receivedUser.lastName!!
                )
                addUser(localUser)
            } else {
                Log.i(TAG, "Login failed")
                loginState.postValue(false)
                userCurrent.postValue(receivedUser)
                loginLevel.postValue(-1)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "LoginViewModel destroyed")
    }

    companion object {
        const val TAG = "LoginViewModel"
    }

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val localUser = repository.getUserData()
            val user = UserData(
                localUser.status,
                localUser.token,
                localUser.userName,
                localUser.email,
                localUser.firstName,
                localUser.lastName
            )
            localData.postValue(user)
        }
    }

    fun getUserDataCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val users = repository.getUserDataCount()
            if (users >= 1) {
                loginState.postValue(true)
                loginStatus.postValue(LoginStatus.SUCCESS)
            } else {
                loginState.postValue(false)
                loginStatus.postValue(LoginStatus.NOT_LOGGED_IN)
            }
        }
    }

    fun addUser(localUser: LocalUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(localUser)
        }
    }

    fun logoutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val localUser = async {
                repository.getUserData()
            }.await()
            async {
                repository.logoutUser(localUser)
            }.await()
            loginLevel.postValue(0)
            loginState.postValue(false)
            loginStatus.postValue(LoginStatus.NOT_LOGGED_IN)
        }
    }

    //get questions
    fun getQuestions() {
        viewModelScope.launch {
            //handle HTTP Errors
            val headerMap = mutableMapOf<String, String?>()
            headerMap["Authorization"] = async {
                val token = repository.getAuthToken()
                if (token == null)
                    null
                else
                    "Token " + token
            }.await()
            if (headerMap["Authorization"] != null) {
                val response =
                    async {
                        try {
                            repository.getQuestions(headerMap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            QuestionResponse()
                            loginStatus.postValue(LoginStatus.FAILURE)
                        }
                    }.await()

            }
        }
    }
}