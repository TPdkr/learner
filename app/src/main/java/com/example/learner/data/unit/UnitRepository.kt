package com.example.learner.data.unit

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
}