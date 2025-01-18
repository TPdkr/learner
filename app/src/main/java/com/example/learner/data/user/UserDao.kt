package com.example.learner.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM userdata WHERE id=1")
    fun getUserData(): Flow<UserEntity>

    @Insert
    suspend fun insert(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Query("UPDATE userdata SET currentCourseId=1, xp=0 WHERE id=1")
    suspend fun reset()
}