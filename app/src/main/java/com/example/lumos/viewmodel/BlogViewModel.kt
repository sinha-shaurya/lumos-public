package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.lumos.repository.BlogRepository

class BlogViewModel(private val repository: BlogRepository) : ViewModel() {
    val blogPost = repository.getBlogPosts().cachedIn(viewModelScope)


    init {
        Log.i(TAG, "BlogViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "BlogViewModel destroyed")
    }

    companion object {
        const val TAG = "BlogViewModel"
    }
}