package com.example.learner.data

import android.content.Context
import com.example.learner.data.course.CourseRepository
import com.example.learner.data.course.OfflineCourseRepository
import com.example.learner.data.relations.unitwithwords.OfflineUnitWithWordsRepository
import com.example.learner.data.relations.unitwithwords.UnitWithWordsRepository
import com.example.learner.data.unit.OfflineUnitRepository
import com.example.learner.data.unit.UnitRepository
import com.example.learner.data.word.OfflineWordRepository
import com.example.learner.data.word.WordRepository

interface AppContainer {
    val wordRepository: WordRepository
    val unitRepository: UnitRepository
    val courseRepository: CourseRepository
    val unitWordRepository: UnitWithWordsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val wordRepository: WordRepository by lazy {
        OfflineWordRepository(LearnerDatabase.getDatabase(context).wordDao())
    }

    override val unitRepository: UnitRepository by lazy {
        OfflineUnitRepository(LearnerDatabase.getDatabase(context).unitDao())
    }

    override val courseRepository: CourseRepository by lazy {
        OfflineCourseRepository(LearnerDatabase.getDatabase(context).courseDao())
    }
    override val unitWordRepository: UnitWithWordsRepository by lazy {
        OfflineUnitWithWordsRepository(
            LearnerDatabase.getDatabase(context).unitWithWordsDao()
        )
    }
}