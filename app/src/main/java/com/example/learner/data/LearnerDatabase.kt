package com.example.learner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.learner.data.course.CourseDao
import com.example.learner.data.course.CourseEntity
import com.example.learner.data.unit.UnitDao
import com.example.learner.data.unit.UnitEntity
import com.example.learner.data.word.WordDao
import com.example.learner.data.word.WordEntity
import kotlin.concurrent.Volatile

@Database(
    entities = [WordEntity::class, UnitEntity::class, CourseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LearnerDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    abstract fun unitDao(): UnitDao

    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var Instance: LearnerDatabase? = null

        fun getDatabase(context: Context): LearnerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LearnerDatabase::class.java, "learner_database")
                    .build().also { Instance = it }
            }
        }
    }
}