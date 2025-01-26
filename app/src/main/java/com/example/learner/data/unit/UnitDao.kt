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

    //delete all references to unit is cross reference table
    @Query("DELETE FROM WordUnitCrossRef WHERE uid = :uid")
    suspend fun deleteUnitMappings(uid: Int)

    //delete all word in a unit from the app
    @Query(
        """
        DELETE FROM words 
        WHERE wid IN (SELECT wid FROM WordUnitCrossRef WHERE uid = :uid)
    """
    )
    suspend fun deleteWordsForUnit(uid: Int)

    //delete all unit data with words in it
    @Transaction
    suspend fun deleteUnitAndWords(uid: Int) {
        deleteWordsForUnit(uid)
        deleteUnitMappings(uid)
        deleteById(uid)
    }

    //delete unit but not the words used in it
    @Transaction
    suspend fun deleteUnitWithoutWords(uid: Int) {
        deleteUnitMappings(uid)
        deleteById(uid)
    }
}