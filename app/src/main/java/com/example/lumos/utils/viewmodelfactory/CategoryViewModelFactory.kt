package com.example.lumos.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.viewmodel.CategoryViewModel

class CategoryViewModelFactory(private val repository: NetworkRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java))
            return CategoryViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}