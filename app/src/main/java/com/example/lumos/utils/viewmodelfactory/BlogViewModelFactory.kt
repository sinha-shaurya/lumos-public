package com.example.lumos.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lumos.repository.BlogRepository
import com.example.lumos.viewmodel.BlogViewModel
import java.lang.IllegalArgumentException

class BlogViewModelFactory(private val repository: BlogRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(BlogViewModel::class.java))
            return BlogViewModel(repository) as T
        throw IllegalArgumentException("Unknown Viewmodel  passed")
    }
}