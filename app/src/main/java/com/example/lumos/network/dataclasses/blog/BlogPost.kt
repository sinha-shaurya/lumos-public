package com.example.lumos.network.dataclasses.blog

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BlogPost(
    @Json(name = "tags") val tags: List<String>,
    @Json(name = "views") val views: Int,
    //used to generate url for blog posts
    @Json(name = "_id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "timeToRead") val readTime: Int,
    @Json(name = "category") val category: String,
    @Json(name = "author") val author: String,
    @Json(name = "aboutAuthor") val aboutAuthor: String,
    @Json(name = "shortDescription") val descriptionShort: String,
    @Json(name = "imageurl") val imageUrl: String,
    @Json(name = "timestamp") val timeStamp: String,
    @Json(name = "__v") val v: Int
)
