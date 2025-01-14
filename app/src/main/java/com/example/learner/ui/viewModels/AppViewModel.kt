package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.Course
import com.example.learner.classes.Lesson
import com.example.learner.data.Catalogue
import com.example.learner.data.course.CourseRepository
import com.example.learner.data.user.UserRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.max

/**app view model determines the main screen and transitions from one screen to the next*/
class AppViewModel(userRepository: UserRepository, courseRepository: CourseRepository) :
    ViewModel() {
    /**lesson chosen*/
    lateinit var currentLesson: Lesson
        private set

    /**chosen course*/
    lateinit var currentCourse: Course
        private set

    /**xp gained*/
    var xp: Int=0
        private set

    init {
        viewModelScope.launch {
            val userData = userRepository.getUserData().filterNotNull().first()
            xp = userData.xp
            val courseId: Int = userData.currentCourseId
            currentCourse = if (courseId == -1) {
                Catalogue.emptyCourse
            } else {
                courseRepository.getCourseWithUnitsAndWords(courseId).filterNotNull().first()
                    .toCourse()
            }
            currentLesson = currentCourse.learnLesson()
        }
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