package com.example.lumos.viewmodel

import androidx.lifecycle.*
import com.example.lumos.local.SavedPost
import com.example.lumos.network.dataclasses.practice.AnsweredQuestion
import com.example.lumos.network.dataclasses.practice.AnsweredQuestionResponse
import com.example.lumos.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(private val repository: NetworkRepository) : ViewModel() {


    private val _submittedAnswers = MutableLiveData<List<AnsweredQuestion>>()
    val submittedAnswers: LiveData<List<AnsweredQuestion>> get() = _submittedAnswers

    private val _points = MutableLiveData<Int>()
    val points: LiveData<Int> get() = _points


    val savedPosts = repository.savedPosts.asLiveData()

    init {
        _submittedAnswers.value = emptyList()
        _points.value = 0
    }

    /**
     * Function to get list of submitted answers and update observable variables accordingly
     * On success sets the value of [_submittedAnswers] , observable through [submittedAnswers]
     * The network request does not proceed if user is not logged in
     */
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
                    _submittedAnswers.postValue(response.answeredQuestionsList)
                    calculatePoints(response)
                    //_points.postValue(points)
                }
            }
        }
    }

    /**
     *  Calculate points based on the list of questions
     *  Updates value of [_points], observable through [points] [LiveData]
     *  All calculations take place on the Default Dispatcher
     */
    private suspend fun calculatePoints(response: AnsweredQuestionResponse) =
        //run coroutine on default dispatcher to ensure max level of parallelism
        withContext(Dispatchers.Default) {
            var points = 0
            for (i in response.answeredQuestionsList ?: emptyList()) {
                val receivedPoints = i.points ?: 0
                points += receivedPoints
            }
            _points.postValue(points)
        }


    fun deletePost(item: SavedPost) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSavedPost(item)
    }

    companion object {
        private const val TAG = "QuestionViewModel"
    }

}