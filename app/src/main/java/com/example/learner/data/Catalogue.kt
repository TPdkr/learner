package com.example.learner.data

import com.example.learner.classes.Course
import com.example.learner.data.Catalogue.courses


/***this section is being drastically changed or replaced atm*/
/**object that stores the courses catalog in [courses] list value*/
object Catalogue {
    val emptyCourse: Course = Course(listOf(), "no course chosen yet")
    val courses: List<Course> = testCourses
}