package com.example.lumos.network

import com.example.lumos.network.api.IsteApi
import com.example.lumos.utils.Constants
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//object to declare class as singleton
object IsteNetworkInstance {
    private val moshi = Moshi.Builder()
        .build()
    private val interceptor = HttpLoggingInterceptor()
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }
    val api: IsteApi by lazy {
        retrofit.create(IsteApi::class.java)
    }

    //set level for logging Interceptor
    init {
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
    }

}