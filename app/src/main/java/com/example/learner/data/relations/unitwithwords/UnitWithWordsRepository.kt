package com.example.learner.data.relations.unitwithwords

interface UnitWithWordsRepository {
    suspend fun addWordToUnit(wid: Int, uid: Int)

    suspend fun removeWordFromUnit(wid: Int, uid: Int)
}