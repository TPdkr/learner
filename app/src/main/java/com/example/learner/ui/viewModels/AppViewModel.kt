package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.learner.classes.Course
import com.example.learner.classes.Lesson
import com.example.learner.data.testCourse
import kotlin.math.max

class AppViewModel : ViewModel() {
    var currentLesson: Lesson
        private set

    var currentCourse: Course
        private set

    var xp: Int
        private set

    init {
        currentLesson = testCourse.learnLesson()
        currentCourse = testCourse
        xp = 0
    }

    fun changeLesson(lesson: Lesson) {
        currentLesson = lesson
    }

    fun switchCourse(course: Course) {
        currentCourse = course
    }

    fun updateScore(inc: Int) {
        xp += max(a = 0, b = inc)
    }


}