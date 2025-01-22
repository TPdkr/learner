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
            //DAOs
            val courseDao = database.courseDao()
            val userDao = database.userDao()

            val tag = "pre-population"
            try {
                //insert user entity
                userDao.insert(UserEntity(0, 1, 0))
                Log.d(tag, "inserted user")
                // Insert default course
                courseDao.insert(CourseEntity(cid = 0, name = "no course chosen yet"))
                Log.d(tag, "inserted courses")
            } catch (e: Exception) {
                Log.e(tag, ("the insertion failed:" + e.message))
            }
        }
    }
}