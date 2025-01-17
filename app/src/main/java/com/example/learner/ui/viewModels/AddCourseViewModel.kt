package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.data.course.CourseEntity
import com.example.learner.data.course.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class AddCourseViewModel(private val courseRepository: CourseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCourseUiState())
    val uiState = _uiState.asStateFlow()

    //the list of names that are not available anymore
    private lateinit var usedNames: List<String>

    //desired name of the course
    private var userInput: String = ""

    //INIT BLOCK
    init {
        viewModelScope.launch {
            courseRepository.getAllCourses().filterNotNull().collect { courses ->
                usedNames = courses.map { it.name.lowercase(Locale.getDefault()) }
            }
        }
    }

    /**change the ui with the user input*/
    fun onValueChange(newValue: String) {
        if (newValue.length <= 30) {
            userInput = newValue
            _uiState.update { currentState ->
                currentState.copy(
                    courseName = userInput,
                    canAdd = userInput.isNotEmpty() && !usedNames.contains(userInput)
                )
            }
        }
    }

    /**insert the entered course into the database*/
    fun addCourse() {
        viewModelScope.launch {
            courseRepository.insert(CourseEntity(0, userInput))
        }
    }

    /**can we add a course?*/
    fun canAdd(): Boolean =
        userInput.isNotEmpty() && !usedNames.contains(userInput)
}

data class AddCourseUiState(
    val courseName: String = "",
    val canAdd: Boolean = false
)