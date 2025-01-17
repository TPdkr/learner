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

    private lateinit var usedNames: List<String>

    private var userInput: String = ""

    init {
        viewModelScope.launch {
            courseRepository.getAllCourses().filterNotNull().collect { courses ->
                usedNames = courses.map { it.name.lowercase(Locale.getDefault()) }
            }
        }
    }

    fun onValueChange(newValue: String) {
        userInput = newValue
        _uiState.update { currentState ->
            currentState.copy(
                courseName = userInput,
                canAdd = userInput.isNotEmpty() && !usedNames.contains(userInput)
            )
        }
    }

    fun addCourse() {
        viewModelScope.launch {
            courseRepository.insert(CourseEntity(0, userInput))
        }
    }

    fun canAdd(): Boolean =
        userInput.isNotEmpty() && !usedNames.contains(userInput)
}

data class AddCourseUiState(
    val courseName: String = "",
    val canAdd: Boolean = false
)