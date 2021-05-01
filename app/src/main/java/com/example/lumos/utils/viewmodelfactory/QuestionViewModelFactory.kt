package com.example.lumos.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.viewmodel.QuestionViewModel

class QuestionViewModelFactory(private val repository: NetworkRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java))
            return QuestionViewModel(repository) as T
        throw IllegalArgumentException("Unknown Viewmodel")
    }
}