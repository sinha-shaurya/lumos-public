package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.lumos.local.SavedPost
import com.example.lumos.network.dataclasses.blog.BlogPost
import com.example.lumos.repository.BlogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlogViewModel(private val repository: BlogRepository) : ViewModel() {
    val blogPost = repository.getBlogPosts().cachedIn(viewModelScope)

    val savedPost = repository.savedPostList.asLiveData()

    init {
        Log.i(TAG, "BlogViewModel created")
    }

    fun savePost(post: BlogPost) = viewModelScope.launch(Dispatchers.IO) {
        repository.savePost(convertToSavedPost(post))
    }

    //convert Blog Post object to Saved post object
    private fun convertToSavedPost(post: BlogPost) =
        SavedPost(
            post.views,
            post.id,
            post.title,
            post.readTime,
            post.category,
            post.author,
            post.aboutAuthor,
            post.descriptionShort,
            post.imageUrl,
            post.timeStamp,
            post.v
        )

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "BlogViewModel destroyed")
    }

    companion object {
        const val TAG = "BlogViewModel"
    }
}