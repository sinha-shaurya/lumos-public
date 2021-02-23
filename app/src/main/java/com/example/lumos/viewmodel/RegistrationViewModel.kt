package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.network.dataclasses.registration.RegistrationData
import com.example.lumos.network.dataclasses.RegistrationResponseData
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
    val level = MutableLiveData<Int>()
    val registrationResponseData = MutableLiveData<RegistrationResponseData>()


    val loginState = MutableLiveData<Boolean>()

    init {
        Log.i(RTAG, "RegistrationViewModel Created")
        loginState.value = false
        registrationResponseData.value =
            RegistrationResponseData(registrationStatus = "unsuccessful")
        level.value = INITIAL
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

    fun register() {
        val registrationData =
            RegistrationData(username, email, password, passwordConfirm, firstName, lastName)
        //initiate network call
        viewModelScope.launch(Dispatchers.IO) {
            val registrationResponse = repository.registerUser(registrationData)
            registrationResponseData.postValue(registrationResponse)
            if (registrationResponse.registrationStatus.equals("successful", ignoreCase = true)){
                //this means we have successfully registered new users
                level.postValue(REGISTER_SUCCESS)
            }
            else if(registrationResponse.registrationStatus.equals("error",ignoreCase = true)){
                //registration has failed
                level.postValue(REGISTER_FAILURE)
            }
        }

    }

    companion object {
        const val RTAG = "RegistrationViewModel"
        const val REGISTER_SUCCESS = 1
        const val REGISTER_FAILURE = -1
        const val INITIAL = 0
    }
}