package com.example.learner.data.course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.learner.data.relations.CourseWithUnits
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses WHERE id=:id")
    fun getCourse(id: Int): Flow<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(courseEntity: CourseEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(courseEntity: CourseEntity)

    @Delete
    suspend fun delete(courseEntity: CourseEntity)

    @Transaction
    @Query("SELECT * FROM courses WHERE id=:id")
    fun getCourseWithUnits(id: Int): Flow<CourseWithUnits>

    @Transaction
    @Query("SELECT * FROM courses")
    fun getAllCoursesWithUnits(): Flow<List<CourseWithUnits>>
}