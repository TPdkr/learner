package com.example.learner.data.relations.unitwithwords

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UnitWithWordsDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(crossRef: WordUnitCrossRef)

    @Delete
    suspend fun delete(crossRef: WordUnitCrossRef)

    @Query("DELETE FROM wordunitcrossref")
    suspend fun clear()
}