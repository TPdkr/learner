package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.learner.classes.Course
import com.example.learner.classes.Lesson
import com.example.learner.data.Catalogue
import kotlin.math.max

/**app view model determines the main screen and transitions from one screen to the next*/
class AppViewModel : ViewModel() {
    /**lesson chosen*/
    var currentLesson: Lesson
        private set

    /**chosen course*/
    var currentCourse: Course
        private set

    /**xp gained*/
    var xp: Int
        private set

    init {
        currentCourse = Catalogue.emptyCourse
        currentLesson = currentCourse.learnLesson()
        xp = 0
    }

    /**change the current lesson to a new value*/
    fun changeLesson(lesson: Lesson) {
        currentLesson = lesson
    }

    /**switch to a different course*/
    fun switchCourse(course: Course) {
        currentCourse = course
    }

    /**increment xp score of the user*/
    fun updateScore(inc: Int) {
        xp += max(a = 0, b = inc)
    }


}