package com.example.learner.data.course

import com.example.learner.data.relations.CourseWithUnits
import com.example.learner.data.relations.CourseWithUnitsAndWords
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    /**get the course by id*/
    fun getCourseStream(id: Int): Flow<CourseEntity>

    /**insert the course into the database*/
    suspend fun insert(courseEntity: CourseEntity)

    /**update the course in the database*/
    suspend fun update(courseEntity: CourseEntity)

    /**delete the course in the database*/
    suspend fun delete(courseEntity: CourseEntity)

    /**get course with its units*/
    fun getCourseWithUnits(id: Int): Flow<CourseWithUnits>

    /**get all courses with units*/
    fun getAllCoursesWithUnits(): Flow<List<CourseWithUnits>>

    /**get course with its units and words*/
    suspend fun getCourseWithUnitsAndWords(id: Int): CourseWithUnitsAndWords

    /**get all courses with their units and words*/
    suspend fun getAllCoursesWithUnitsAndWords(): List<CourseWithUnitsAndWords>
}