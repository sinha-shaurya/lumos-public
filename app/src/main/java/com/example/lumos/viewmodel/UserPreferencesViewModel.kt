package com.example.lumos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.lumos.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    val userPreferences = userPreferencesRepository.userPreferencesFlow.asLiveData()

    fun changeTheme(theme: Int) = viewModelScope.launch(Dispatchers.IO) {
        userPreferencesRepository.changeTheme(theme)
    }

}