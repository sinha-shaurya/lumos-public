package com.example.lumos.repository

import androidx.annotation.WorkerThread
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.lumos.local.SavedPost
import com.example.lumos.local.UserDao
import com.example.lumos.network.BlogPostNetworkInstance
import com.example.lumos.network.adapters.BlogPagingSource

class BlogRepository(val userDao: UserDao) {
    fun getBlogPosts() = Pager(
        config = PagingConfig(
            pageSize = BLOG_PAGE_SIZE,
            maxSize = 100,
            enablePlaceholders = false,
            initialLoadSize = BLOG_PAGE_SIZE
        ),
        pagingSourceFactory = {
            BlogPagingSource(BlogPostNetworkInstance.blogApi)
        }
    ).flow


    val savedPostList = userDao.getSavedPosts()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun savePost(post: SavedPost) = userDao.savePost(post)

    suspend fun deletePost(post: SavedPost) = userDao.deleteSavePost(post)

    //Blog Fetches 10 posts at a time
    companion object {
        private const val BLOG_PAGE_SIZE = 8
    }

    suspend fun checkExists(id: String) = userDao.checkPost(postId = id)
}