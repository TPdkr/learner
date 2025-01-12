package com.example.learner.data.word

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**data access object for word entity and word data class*/
@Dao
interface WordDao {
    @Query("SELECT * FROM words WHERE id=:id")
    fun getWord(id: Int): Flow<WordEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(wordEntity: WordEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(wordEntity: WordEntity)

    @Delete
    suspend fun delete(wordEntity: WordEntity)
}