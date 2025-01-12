package com.example.learner.data.course

import kotlinx.coroutines.flow.Flow

class OfflineCourseRepository(private val courseDao: CourseDao) : CourseRepository {
    override fun getCourseStream(id: Int): Flow<CourseEntity> = courseDao.getCourse(id)

    override suspend fun insert(courseEntity: CourseEntity) = courseDao.insert(courseEntity)

    override suspend fun update(courseEntity: CourseEntity) = courseDao.update(courseEntity)

    override suspend fun delete(courseEntity: CourseEntity) = courseDao.delete(courseEntity)
}