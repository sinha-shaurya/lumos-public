package com.example.lumos.network.api

import com.example.lumos.network.dataclasses.blog.BlogPost
import retrofit2.http.GET
import retrofit2.http.Query

interface BlogApi {

    @GET("blogPosts")
    suspend fun getPost(@Query(value = "page") page: Int): List<BlogPost>
}