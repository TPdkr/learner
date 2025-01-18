package com.example.learner.data.unit

import com.example.learner.data.relations.unitwithwords.UnitWithWords
import kotlinx.coroutines.flow.Flow

interface UnitRepository {
    /**get the unit by id*/
    fun getUnitStream(id: Int): Flow<UnitEntity>

    /**insert unit into the database*/
    suspend fun insert(unitEntity: UnitEntity)

    /**update unit in the database*/
    suspend fun update(unitEntity: UnitEntity)

    /**delete unit in the database*/
    suspend fun delete(unitEntity: UnitEntity)

    /**get a unit with corresponding list of words*/
    fun getUnitWithWords(id: Int): Flow<UnitWithWords>

    /**get the number of units in a course*/
    fun getUnitCount(id: Int): Flow<Int>
}