package com.example.learner.data.relations.unitwithwords

class OfflineUnitWithWordsRepository(private val unitWithWordsDao: UnitWithWordsDao): UnitWithWordsRepository {
    override suspend fun addWordToUnit(wid: Int, uid: Int) {
        unitWithWordsDao.insert(WordUnitCrossRef(wid, uid))
    }

    override suspend fun removeWordFromUnit(wid: Int, uid: Int) {
        unitWithWordsDao.delete(WordUnitCrossRef(wid, uid))
    }

    override suspend fun clear() = unitWithWordsDao.clear()
}