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

/**this view model supports the units page of the course. it takes a course as input*/
class CourseUnitViewModel(course: Course) : ViewModel() {
    //the ui state is only readable to the user
    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    //the chosen unit to display
    var chosenUnit by mutableStateOf(testUnit)
        private set

    init {
        resetView(course)
    }

    /**choose a different unit do display*/
    fun chooseUnit(unit: CourseUnit) {
        chosenUnit = unit
    }

    /**set the state of the view model to the one we got from input*/
    private fun resetView(course: Course) {
        _uiState.value = CourseUiState(units = course.units, courseName = course.name)
    }
}

/**stores the ui information about the state of the units screen*/
data class CourseUiState(
    val units: List<CourseUnit> = listOf(),
    val courseName: String = "",
)