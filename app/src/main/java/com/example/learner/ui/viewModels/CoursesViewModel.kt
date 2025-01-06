package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.learner.classes.Course
import com.example.learner.data.Catalogue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CoursesViewModel(val appViewModel: AppViewModel): ViewModel() {
    private val _uiState = MutableStateFlow(CoursesUiState(appViewModel.currentCourse, Catalogue.courses))
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    fun switchCourse() {
        _uiState.update { currentState->
            currentState.copy(currentCourse = appViewModel.currentCourse, courses= Catalogue.courses)
        }
    }
}

data class CoursesUiState(
    val currentCourse: Course,
    val courses: List<Course>
)