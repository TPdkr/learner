package com.example.learner.data

import android.content.Context
import com.example.learner.data.unit.OfflineUnitRepository
import com.example.learner.data.unit.UnitRepository
import com.example.learner.data.word.OfflineWordRepository
import com.example.learner.data.word.WordRepository

interface AppContainer {
    val wordRepository: WordRepository

    val unitRepository: UnitRepository
}

class AppDataContainer(private val context: Context): AppContainer{
    override val wordRepository: WordRepository by lazy {
        OfflineWordRepository(LearnerDatabase.getDatabase(context).wordDao())
    }

    override val unitRepository: UnitRepository by lazy {
        OfflineUnitRepository(LearnerDatabase.getDatabase(context).unitDao())
    }
}