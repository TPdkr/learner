package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.learner.classes.Course
import com.example.learner.data.testCourses
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CoursesViewModel(val appViewModel: AppViewModel): ViewModel() {
    private val _uiState = MutableStateFlow(CoursesUiState(appViewModel.currentCourse, testCourses))
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    fun switchCourse() {
        _uiState.update { currentState->
            currentState.copy(currentCourse = appViewModel.currentCourse, courses= testCourses)
        }
    }
}

data class CoursesUiState(
    val currentCourse: Course,
    val courses: List<Course>
)