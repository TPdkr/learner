package com.example.learner.data.course

import com.example.learner.data.relations.CourseWithUnits
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

    fun getCourseWithUnits(id: Int): Flow<CourseWithUnits>

    fun getAllCoursesWithUnits(): Flow<List<CourseWithUnits>>
}