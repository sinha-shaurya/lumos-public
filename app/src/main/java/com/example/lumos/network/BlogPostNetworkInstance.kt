package com.example.lumos.network

import com.example.lumos.network.api.BlogApi
import com.example.lumos.utils.Constants
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object BlogPostNetworkInstance {
    private val moshi = Moshi.Builder().build()
    private val interceptor = HttpLoggingInterceptor()
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(Constants.BLOG_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).client(client).build()
    }
    val blogApi: BlogApi by lazy {
        retrofit.create(BlogApi::class.java)
    }

    init {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}