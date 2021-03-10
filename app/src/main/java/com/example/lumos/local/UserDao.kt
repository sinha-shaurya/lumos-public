package com.example.lumos.local

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(localUser: LocalUser)

    @Query("SELECT COUNT(*) from user_data")
    suspend fun getUserDataCount(): Int

    @Query("SELECT * FROM user_data")
    suspend fun getUserData(): LocalUser

    @Delete
    suspend fun logoutUser(localUser: LocalUser)

    @Query("SELECT token from user_data ORDER BY id LIMIT 1")
    suspend fun getAuthToken(): String?
}