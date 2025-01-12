package com.example.learner.data.unit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Query("SELECT * FROM units WHERE id=:id")
    fun getUnit(id: Int): Flow<UnitEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(unitEntity: UnitEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(unitEntity: UnitEntity)

    @Delete
    suspend fun delete(unitEntity: UnitEntity)
}