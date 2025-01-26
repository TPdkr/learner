package com.example.learner.data.course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.learner.data.relations.CourseWithUnitsAndWords
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses WHERE cid=:id")
    fun getCourse(id: Int): Flow<CourseEntity>

    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(courseEntity: CourseEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(courseEntity: CourseEntity)

    @Delete
    suspend fun delete(courseEntity: CourseEntity)

    @Transaction
    @Query("SELECT * FROM courses WHERE cid = :id")
    fun getCourseWithUnitsAndWords(id: Int): Flow<CourseWithUnitsAndWords>

    @Transaction
    @Query("SELECT * FROM courses")
    fun getAllCoursesWithUnitsAndWords(): Flow<List<CourseWithUnitsAndWords>>

    @Transaction
    @Query(
        """
    SELECT * FROM courses 
    WHERE cid = (SELECT currentCourseId FROM userdata LIMIT 1)
"""
    )
    fun getCurrentCourseWithUnitsAndWords(): Flow<CourseWithUnitsAndWords>

    @Query("DELETE FROM courses")
    suspend fun clear()

    //delete course be ID
    @Query("DELETE FROM courses WHERE cid=:cid")
    suspend fun deleteById(cid: Int)

    //delete course units
    @Query("DELETE FROM units WHERE courseId = :cid")
    suspend fun deleteUnits(cid: Int)

    //delete all words cross-references associated with the course
    @Query(
        """
        DELETE FROM WordUnitCrossRef 
        WHERE uid IN (SELECT uid FROM units WHERE courseId = :cid)
    """
    )
    suspend fun deleteWordMappings(cid: Int)

    //delete all words associated with the course
    @Query(
        """
        DELETE FROM words 
        WHERE wid IN (
            SELECT wid 
            FROM WordUnitCrossRef 
            WHERE uid IN (SELECT uid FROM units WHERE courseId = :cid)
        )
    """
    )
    suspend fun deleteWords(cid: Int)

    @Query("UPDATE userdata SET currentCourseId=1 WHERE id=1")
    suspend fun resetCurrentCourse()

    @Transaction
    suspend fun deleteCourseAndWords(cid: Int) {
        deleteWords(cid)
        deleteWordMappings(cid)
        deleteUnits(cid)
        deleteById(cid)
        resetCurrentCourse()
    }

    @Transaction
    suspend fun deleteCourseWithoutWords(cid: Int) {
        deleteWordMappings(cid)
        deleteUnits(cid)
        deleteById(cid)
        resetCurrentCourse()
    }
}