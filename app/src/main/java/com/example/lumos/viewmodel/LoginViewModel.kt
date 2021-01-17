package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.network.dataclasses.LoginUserData
import com.example.lumos.network.dataclasses.UserData
import com.example.lumos.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: NetworkRepository) : ViewModel() {
    val loginState = MutableLiveData<Boolean>()
    val userCurrent = MutableLiveData<UserData>()

    init {
        Log.i(TAG, "LoginViewModel created")
        loginState.value = false//in future perform a database call
        userCurrent.value = null
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
                //also trigger database call or preference datastore storage
            } else {
                loginState.postValue(false)
                Log.i(TAG, "Login failed")
                userCurrent.postValue(receivedUser)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG,"LoginViewModel destroyed")
    }
    companion object {
        const val TAG = "LoginViewModel"
    }
}