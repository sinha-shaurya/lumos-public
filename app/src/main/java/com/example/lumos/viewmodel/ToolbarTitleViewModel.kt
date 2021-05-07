package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This is the ViewModel to store the changes to custom toolbar title
 * Following is a workaround until a solution is available without requiring ViewModels to change the Toolbar title
 */
class ToolbarTitleViewModel : ViewModel() {

    private val _title = MutableLiveData<String>("Technical Prophet.")
    val title: LiveData<String>
        get() = _title

    fun changeTitle(newTitle: String) {

        _title.value = newTitle

        Log.i(TAG, newTitle)
        Log.i(TAG, _title.value!!)
        Log.i(TAG, title.value!!)
    }

    companion object {
        const val TAG = "ToolbarTitleViewModel"
    }
}