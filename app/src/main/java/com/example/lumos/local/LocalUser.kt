package com.example.lumos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class LocalUser(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val status: String,
    val token: String,
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String
)