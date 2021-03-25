package com.example.lumos.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.viewmodel.LoginViewModel

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val repository: NetworkRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}