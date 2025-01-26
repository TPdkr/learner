package com.example.learner.data.unit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.learner.data.relations.unitwithwords.UnitWithWords
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Query("SELECT * FROM units WHERE uid=:id")
    fun getUnit(id: Int): Flow<UnitEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(unitEntity: UnitEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(unitEntity: UnitEntity)

    @Delete
    suspend fun delete(unitEntity: UnitEntity)

    @Transaction
    @Query("SELECT * FROM units WHERE uid=:id")
    fun getUnitWithWords(id: Int): Flow<UnitWithWords>

    @Query("SELECT COUNT(*) AS unitCount FROM units WHERE courseId=:id LIMIT 1")
    fun getUnitCount(id: Int): Flow<Int>

    @Query("DELETE FROM units")
    suspend fun clear()

    @Query("DELETE FROM units WHERE uid=:id")
    suspend fun deleteById(id: Int)
}