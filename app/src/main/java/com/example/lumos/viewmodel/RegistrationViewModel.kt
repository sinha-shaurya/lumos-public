package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.network.dataclasses.RegistrationData
import com.example.lumos.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: NetworkRepository) : ViewModel() {
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordConfirm: String
    private lateinit var username: String

    private lateinit var registrationData: RegistrationData

    val loginState = MutableLiveData<Boolean>()

    init {
        Log.i(RTAG, "RegistrationViewModel Created")
        loginState.value = false
    }

    //for first step
    fun registerFirst(firstname: String, lastname: String, email: String) {
        firstName = firstname
        lastName = lastname
        this.email = email
    }

    fun registerSecond(username: String, password1: String, password2: String) {
        this.username = username
        password = password1
        passwordConfirm = password2
    }

    fun Register() {
        registrationData =
            RegistrationData(username, email, password, passwordConfirm, firstName, lastName)
        viewModelScope.launch(Dispatchers.IO) {
            val registrationResponseData = repository.registerUser(registrationData)
            //write token to database or datastore
            if (registrationResponseData.registrationStatus.equals("success")) {
                loginState.postValue(true)
            } else {
                loginState.postValue(false)
            }
        }
    }

    companion object {
        const val RTAG = "RegistrationViewModel"
    }
}