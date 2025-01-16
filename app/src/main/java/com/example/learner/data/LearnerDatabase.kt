package com.example.learner.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.learner.data.course.CourseDao
import com.example.learner.data.course.CourseEntity
import com.example.learner.data.relations.unitwithwords.UnitWithWordsDao
import com.example.learner.data.relations.unitwithwords.WordUnitCrossRef
import com.example.learner.data.unit.UnitDao
import com.example.learner.data.unit.UnitEntity
import com.example.learner.data.user.UserDao
import com.example.learner.data.user.UserEntity
import com.example.learner.data.word.WordDao
import com.example.learner.data.word.WordEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.concurrent.Volatile

@Database(
    entities = [WordEntity::class, UnitEntity::class, CourseEntity::class, UserEntity::class, WordUnitCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class LearnerDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    abstract fun unitDao(): UnitDao

    abstract fun courseDao(): CourseDao

    abstract fun unitWithWordsDao(): UnitWithWordsDao

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: LearnerDatabase? = null

        fun getDatabase(context: Context): LearnerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LearnerDatabase::class.java, "learner_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build().also { Instance = it }

            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insert data in a background thread
                Executors.newSingleThreadExecutor().execute {
                    Instance?.let { database ->
                        // Prepopulate the database
                        CoroutineScope(Dispatchers.IO).launch {
                            prepopulateDatabase(database)
                        }
                    }
                }
            }
        }

        private suspend fun prepopulateDatabase(database: LearnerDatabase) {
            val courseDao = database.courseDao()
            val unitDao = database.unitDao()
            val wordDao = database.wordDao()
            val unitToWordsDao = database.unitWithWordsDao()
            val userDao = database.userDao()

            val tag = "pre-population"
            try {
                userDao.insert(UserEntity(0, 1, 0))

                // Insert Courses
                val courses = listOf(
                    CourseEntity(cid = 0, name = "no course chosen yet"),
                    CourseEntity(cid = 0, name = "German Basics"),
                    CourseEntity(cid = 0, name = "Advanced German")
                )
                courses.forEach {
                    courseDao.insert(it)
                    Log.d(tag, "inserted course: " + it.name)
                }
                Log.d(tag, "inserted courses")
                // Insert Units
                val units = listOf(
                    UnitEntity(
                        uid = 0,
                        name = "test unit",
                        desc = "Learn the alphabet",
                        number = 1,
                        courseId = 2
                    ),
                    UnitEntity(
                        uid = 0,
                        name = "test unit 2",
                        desc = "Learn greetings and introductions",
                        number = 2,
                        courseId = 2
                    )
                )
                units.forEach {
                    unitDao.insert(it)
                    Log.d(tag, "inserted unit: " + it.name)
                }
                Log.d(tag, "inserted units")

                // Insert Words
                val words = listOf(
                    WordEntity(
                        wid = 0,
                        german = "Hallo",
                        gender = -1,
                        plural = -1,
                        revision = 0,
                        revisionTime = 0,
                        translation = "hello"
                    ),
                    WordEntity(
                        wid=0,
                        german = "Guten Morgen",
                        gender = -1,
                        plural = -1,
                        revision = 0,
                        revisionTime = 0,
                        translation = "good morning",
                    )
                )
                words.forEach {
                    wordDao.insert(it)
                    Log.d(tag, "inserted word: " + it.german)
                }
                Log.d(tag, "inserted words")

                val wordsToUnits = listOf(
                    WordUnitCrossRef(1, 2),
                    WordUnitCrossRef(2, 2)
                )
                wordsToUnits.forEach { unitToWordsDao.insert(it) }
                Log.d(tag, "inserted cross ref")
            } catch(e: Exception){
                Log.e(tag, ("the insertion failed:" + e.message))
            }

        }
    }
}