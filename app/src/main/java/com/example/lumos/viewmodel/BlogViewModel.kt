package com.example.lumos.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.lumos.local.SavedPost
import com.example.lumos.network.dataclasses.blog.BlogPost
import com.example.lumos.repository.BlogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlogViewModel(private val repository: BlogRepository) : ViewModel() {

    //expose Flow of Blog Posts as LiveData for easier observation via viewholder
    val blogPost = repository.getBlogPosts()
        .asLiveData()
        .cachedIn(viewModelScope)

    //parameter for search request within the database
    val postId = MutableLiveData<String>("")

    //output of query
    val exists=MutableLiveData<Boolean>(false)


    init {
        Log.i(TAG, "BlogViewModel created")
    }

    //function to save an instance of SavedPost to BlogPost
    fun savePost(post: BlogPost) = viewModelScope.launch(Dispatchers.IO) {
        repository.savePost(convertToSavedPost(post))
        exists.postValue(true)
    }

    //function to delete corresponding SavedPost object from database when a BlogPost object is specified
    fun deletePost(post: BlogPost) = viewModelScope.launch(Dispatchers.IO) {
        repository.deletePost(convertToSavedPost(post))
        exists.postValue(false)
    }

    //function to search for a given post in a database, and emit values to corresponding boolean output which then be observed by the viewholder
    fun searchPost()=viewModelScope.launch(Dispatchers.IO) {
        val check=repository.checkExists(postId.value!!)
        exists.postValue(check)
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