package com.example.lumos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blog_data")
data class SavedPost(
    val views: Int,
    @PrimaryKey val id: String,
    val title: String,
    val readTime: Int,
    val category: String,
    val author: String,
    val aboutAuthor: String,
    val descriptionShort: String,
    val imageUrl: String,
    val timestamp: String,
    val v: Int
)