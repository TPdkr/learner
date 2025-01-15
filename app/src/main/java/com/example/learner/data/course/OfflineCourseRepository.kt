package com.example.learner.data.course

import com.example.learner.data.relations.CourseWithUnitsAndWords
import kotlinx.coroutines.flow.Flow

class OfflineCourseRepository(private val courseDao: CourseDao) : CourseRepository {
    override fun getCourseStream(id: Int): Flow<CourseEntity> = courseDao.getCourse(id)

    override suspend fun insert(courseEntity: CourseEntity) = courseDao.insert(courseEntity)

    override suspend fun update(courseEntity: CourseEntity) = courseDao.update(courseEntity)

    override suspend fun delete(courseEntity: CourseEntity) = courseDao.delete(courseEntity)

    /*override fun getAllCoursesWithUnits(): Flow<List<CourseWithUnits>> =
        courseDao.getAllCoursesWithUnits()

    override fun getCourseWithUnits(id: Int): Flow<CourseWithUnits> =
        courseDao.getCourseWithUnits(id)*/

    override fun getCourseWithUnitsAndWords(id: Int): Flow<CourseWithUnitsAndWords> {
        return courseDao.getCourseWithUnitsAndWords(id)
    }

    override fun getAllCoursesWithUnitsAndWords(): Flow<List<CourseWithUnitsAndWords>> {
        return courseDao.getAllCoursesWithUnitsAndWords()
    }

}