package com.example.lumos.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.lumos.network.BlogPostNetworkInstance
import com.example.lumos.network.paging.BlogPagingSource

class BlogRepository() {
    fun getBlogPosts() = Pager(
        config = PagingConfig(pageSize = 5, maxSize = 100, enablePlaceholders = false),
        pagingSourceFactory = {
            BlogPagingSource(BlogPostNetworkInstance.blogApi)
        }
    ).liveData
}