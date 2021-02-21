package com.example.lumos.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.lumos.network.BlogPostNetworkInstance
import com.example.lumos.network.paging.BlogPagingSource

class BlogRepository() {
    fun getBlogPosts() = Pager(
        config = PagingConfig(pageSize = BLOG_PAGE_SIZE, maxSize = 100, enablePlaceholders = false,initialLoadSize = BLOG_PAGE_SIZE),
        pagingSourceFactory = {
            BlogPagingSource(BlogPostNetworkInstance.blogApi)
        }
    ).liveData

    //Blog Fetches 10 posts at a time
    companion object{
        private const val BLOG_PAGE_SIZE=10
    }
}