package com.example.learner.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.learner.classes.Course
import com.example.learner.classes.CourseUnit
import com.example.learner.data.testUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CourseUnitViewModel(course: Course): ViewModel() {
    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    var chosenUnit by mutableStateOf(testUnit)
        private set

    init{
        resetView(course)
    }

    fun chooseUnit(unit: CourseUnit){
        chosenUnit=unit
    }
    //set the state of the view model to the one we got from input
    private fun resetView(course: Course){
        _uiState.value = CourseUiState(units = course.units, courseName = course.name)
    }
}

data class CourseUiState(
    val units: List<CourseUnit> = listOf(),
    val courseName: String="",
)