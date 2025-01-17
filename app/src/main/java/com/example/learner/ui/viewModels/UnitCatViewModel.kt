package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.Course
import com.example.learner.classes.CourseUnit
import com.example.learner.data.course.CourseRepository
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
    private val _uiState = MutableStateFlow(UnitCatUiState())
    val uiState: StateFlow<UnitCatUiState> = _uiState.asStateFlow()


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
data class UnitCatUiState(
    val units: List<CourseUnit> = listOf(),
    val courseName: String = "",
)