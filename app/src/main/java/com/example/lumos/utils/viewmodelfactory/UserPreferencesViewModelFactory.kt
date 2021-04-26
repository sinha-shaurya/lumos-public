package com.example.lumos.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lumos.repository.UserPreferencesRepository
import com.example.lumos.viewmodel.UserPreferencesViewModel

class UserPreferencesViewModelFactory(val userPreferencesRepository: UserPreferencesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java))
            return UserPreferencesViewModel(userPreferencesRepository) as T
        else
            throw IllegalArgumentException("Unknown viewmodel")
    }

}