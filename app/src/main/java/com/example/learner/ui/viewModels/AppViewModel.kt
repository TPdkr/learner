package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.learner.classes.Course
import com.example.learner.classes.Lesson
import com.example.learner.data.testCourse
import com.example.learner.data.testLesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    var currentLesson: Lesson
        private set

    var currentCourse: Course
        private set

    init {
        currentLesson = testLesson
        currentCourse = testCourse
    }

    fun changeLesson(lesson: Lesson){
        currentLesson = lesson
    }

    fun switchCourse(course: Course){
        currentCourse=course
    }


}

data class AppUiState(
    val lesson: Lesson = Lesson(listOf()),//maybe make the var private set instead?
    val course: Course = Course(listOf(), "")
)