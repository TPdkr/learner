package com.example.learner.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.Course
import com.example.learner.classes.CourseUnit
import com.example.learner.data.course.CourseRepository
import com.example.learner.data.testUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**this view model supports the units page of the course. it takes a course as input*/
class CourseUnitViewModel(courseRepository: CourseRepository, savedStateHandle: SavedStateHandle) :
    ViewModel() {
    init {
        viewModelScope.launch {
            val courseId: Int = 0//TODO
            val course =
                courseRepository.getCourseWithUnitsAndWords(courseId)
                    .filterNotNull().first().toCourse()
            resetView(course)
        }
    }

    //the ui state is only readable to the user
    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    //the chosen unit to display
    var chosenUnit by mutableStateOf(testUnit)
        private set


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