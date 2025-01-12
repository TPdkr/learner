package com.example.learner.data

import android.content.Context

interface AppContainer {
    val wordRepository: LearnerRepository
}

class AppDataContainer(private val context: Context): AppContainer{
    override val wordRepository: LearnerRepository by lazy {
        OfflineLearnerRepository(LearnerDatabase.getDatabase(context).wordDao())
    }
}