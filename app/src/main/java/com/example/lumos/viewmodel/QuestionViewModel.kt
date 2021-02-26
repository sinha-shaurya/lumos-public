package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.network.dataclasses.practice.QuestionResponse
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class QuestionViewModel(private val repository: NetworkRepository) : ViewModel() {
    val questionResponse = MutableLiveData<QuestionResponse>()
    val loadingStatus = MutableLiveData<LoadingStatus>()
    val token = MutableLiveData<String>()

    init {
        token.value = null
        loadingStatus.value = LoadingStatus.LOADING
        questionResponse.value = null
        getQuestions()
    }

    fun getAuthToken() {
        viewModelScope.launch(Dispatchers.IO) {
            val authToken = repository.getAuthToken()
            token.postValue(authToken)
        }
        Log.i("QuestionViewModel", token.value ?: "Empty")
    }

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
                        }

                    }.await()
                if (response.questionStatus.equals("successful", ignoreCase = true)) {
                    questionResponse.postValue(response)
                    loadingStatus.postValue(LoadingStatus.SUCCESS)
                } else
                    loadingStatus.postValue(LoadingStatus.FAILURE)
            } else
                loadingStatus.postValue(LoadingStatus.FAILURE)
        }

    }

}