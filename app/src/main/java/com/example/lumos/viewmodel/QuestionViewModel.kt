package com.example.lumos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.network.dataclasses.practice.AnsweredQuestion
import com.example.lumos.network.dataclasses.practice.AnsweredQuestionResponse
import com.example.lumos.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(private val repository: NetworkRepository) : ViewModel() {


    private val _submittedAnswer = MutableLiveData<List<AnsweredQuestion>>()
    val submittedAnswer: LiveData<List<AnsweredQuestion>> get() = _submittedAnswer

    private val _points = MutableLiveData<Int>()
    val points: LiveData<Int> get() = _points

    init {
        _submittedAnswer.value = emptyList()
        _points.value = 0
    }

    fun getSubmittedAnswer() {
        viewModelScope.launch {
            val token = repository.getAuthToken()
            //now construct headermap
            val headerMap = mutableMapOf<String, String>()
            if (token != null) {
                //only proceed if user is logged in
                headerMap["Authorization"] = "Token $token"
                val response = try {
                    repository.getSubmittedAnswer(headerMap)
                } catch (e: Exception) {
                    null
                }
                if (response != null) {
                    _submittedAnswer.postValue(response.answeredQuestionsList)
                    var points = 0
                    for (i in response.answeredQuestionsList ?: emptyList()) {
                        val receivedPoints = i.points ?: 0
                        points += receivedPoints
                    }
                    calculatePoints(response)
                    //_points.postValue(points)
                }
            }
        }
    }

    suspend fun calculatePoints(response: AnsweredQuestionResponse) =
        //run coroutine on default dispatcher to ensure max level of parallelism
        withContext(Dispatchers.Default) {
            var points = 0
            for (i in response.answeredQuestionsList ?: emptyList()) {
                val receivedPoints = i.points ?: 0
                points += receivedPoints
            }
            _points.postValue(points)
        }

    companion object {
        private const val TAG = "QuestionViewModel"
    }
}