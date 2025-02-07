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
    @Query("SELECT * FROM words WHERE wid=:id")
    fun getWord(id: Int): Flow<WordEntity>

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordEntity>>

    @Query("SELECT COUNT(*) FROM words WHERE revision=-1 LIMIT 1")
    fun getDoneWordCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(wordEntity: WordEntity): Long

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(wordEntity: WordEntity)

    @Delete
    suspend fun delete(wordEntity: WordEntity)

    @Query("DELETE FROM words")
    suspend fun clear()

    @Query("DELETE FROM words WHERE wid = :id")
    suspend fun deleteById(id: Int)
}