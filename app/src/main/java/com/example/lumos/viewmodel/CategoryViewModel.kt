package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumos.network.dataclasses.events.CategoryResponse
import com.example.lumos.network.dataclasses.events.Events
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(private val repository: NetworkRepository) : ViewModel() {
    val categoryList = MutableLiveData<CategoryResponse>()
    val error = MutableLiveData<String>()
    val loadingStatus = MutableLiveData<LoadingStatus>()

    private val _eventList=MutableLiveData<List<Events>>()
    val eventList: LiveData<List<Events>>
        get()=_eventList

    init {
        Log.i(TAG, "CategoryViewModel created")
        //initialise categoryList to null
        categoryList.value = null
        _eventList.value= emptyList()
        error.value = "OK"
        loadingStatus.value = LoadingStatus.LOADING
        getList()
    }

    fun getList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCategory()
                Log.i(TAG, "Network request triggered")
                categoryList.postValue(response)
                loadingStatus.postValue(LoadingStatus.SUCCESS)
            } catch (e: Exception) {
                error.postValue(e.message)
                Log.i(TAG, e.message ?: "Error")
                loadingStatus.postValue(LoadingStatus.FAILURE)
            }
        }
    }

    fun resetToLoading(){
        loadingStatus.postValue(LoadingStatus.LOADING)
    }

    fun getEventList(nameSlug:String){
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    companion object {
        const val TAG = "CategoryViewModel created"
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "CategoryViewModel destroyed")
    }
}