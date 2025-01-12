package com.example.learner.data.unit

import com.example.learner.data.word.WordEntity
import kotlinx.coroutines.flow.Flow

class OfflineUnitRepository(private val unitDao: UnitDao) : UnitRepository {
    override fun getUnitStream(id: Int): Flow<UnitEntity> = unitDao.getUnit(id)

    override suspend fun insert(unitEntity: UnitEntity) = unitDao.insert(unitEntity)

    override suspend fun update(unitEntity: UnitEntity) = unitDao.update(unitEntity)

    override suspend fun delete(unitEntity: UnitEntity) = unitDao.delete(unitEntity)
}