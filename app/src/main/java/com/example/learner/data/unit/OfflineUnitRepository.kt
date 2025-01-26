package com.example.learner.data.unit

import com.example.learner.data.relations.unitwithwords.UnitWithWords
import kotlinx.coroutines.flow.Flow

class OfflineUnitRepository(private val unitDao: UnitDao) : UnitRepository {
    override fun getUnitStream(id: Int): Flow<UnitEntity> = unitDao.getUnit(id)

    override suspend fun insert(unitEntity: UnitEntity) = unitDao.insert(unitEntity)

    override suspend fun update(unitEntity: UnitEntity) = unitDao.update(unitEntity)

    override suspend fun delete(unitEntity: UnitEntity) = unitDao.delete(unitEntity)

    override fun getUnitWithWords(id: Int): Flow<UnitWithWords> = unitDao.getUnitWithWords(id)

    override fun getUnitCount(id: Int): Flow<Int> = unitDao.getUnitCount(id)

    override suspend fun clear() = unitDao.clear()

    override suspend fun deleteById(id: Int) = unitDao.deleteById(id)

    override suspend fun deleteUnitAndWords(id: Int) = unitDao.deleteUnitAndWords(id)

    override suspend fun deleteUnitWithoutWords(id: Int) = unitDao.deleteUnitWithoutWords(id)
}