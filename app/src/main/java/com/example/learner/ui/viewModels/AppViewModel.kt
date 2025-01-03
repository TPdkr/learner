package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.learner.classes.Course
import com.example.learner.classes.Lesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

}

data class AppUiState(
    val lesson: Lesson = Lesson(listOf(), listOf()),
    val course: Course = Course(listOf(), "")
)