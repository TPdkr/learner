package com.example.learner.data.word

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**data access object for word entity and word data class*/
@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(wordEntity: WordEntity)

    @Update
    suspend fun update(wordEntity: WordEntity)

    @Delete
    suspend fun delete(wordEntity: WordEntity)
}