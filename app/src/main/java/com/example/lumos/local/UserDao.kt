package com.example.lumos.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * from blog_data")
    fun getSavedPosts(): Flow<List<SavedPost>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePost(post: SavedPost)

    @Delete
    suspend fun deleteSavePost(post: SavedPost)

    @Query("SELECT EXISTS(SELECT 1 from blog_data where id LIKE :postId)")
    suspend fun checkPost(postId: String): Boolean
}