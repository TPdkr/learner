package com.example.learner.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val LOG_TAG = "unit screen view model"

/**this view model supports the units page of the course. it takes a course as input*/
class UnitCatViewModel(courseRepository: CourseRepository) :
    ViewModel() {
    //INIT BLOCK - we collect database state and transfer it to the ui State
    init {
        viewModelScope.launch {
            try {
                courseRepository.getCurrentCourse().filterNotNull()
                    .collect { courseWithUnitsAndWords ->
                        val course = courseWithUnitsAndWords.toCourse()
                        updateView(course)
                    }
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message ?: "no message given")
            }
        }
    }

    //the ui state is only readable to the user
    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    //the chosen unit to display
    private var chosenUnit by mutableStateOf(testUnit)


    /**choose a different unit do display*/
    fun chooseUnit(unit: CourseUnit) {
        chosenUnit = unit
        _uiState.update { currentState ->
            currentState.copy(showUnit = true, chosenUnit = chosenUnit)
        }
    }

    /**hide unit*/
    fun hideUnit() {
        _uiState.update { currentState ->
            currentState.copy(showUnit = false)
        }
    }

    /**set the state of the view model to the one we got from the database*/
    private fun updateView(course: Course) {
        _uiState.update { currentState ->
            currentState.copy(
                units = course.units,
                courseName = course.name
            )
        }
    }
}

/**stores the ui information about the state of the units screen*/
data class CourseUiState(
    val units: List<CourseUnit> = listOf(),
    val courseName: String = "",
    val showUnit: Boolean = false,
    val chosenUnit: CourseUnit = CourseUnit(listOf(), "", 1, "")
)