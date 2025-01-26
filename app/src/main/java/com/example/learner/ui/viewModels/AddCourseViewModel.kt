package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.data.course.CourseEntity
import com.example.learner.data.course.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class AddCourseViewModel(private val courseRepository: CourseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCourseUiState())
    val uiState = _uiState.asStateFlow()

    //the list of names that are not available anymore
    private lateinit var usedNames: List<String>

    //the course to edit if needed
    private var courseId: Int = AppData.CourseId

    //desired name of the course
    private var userInput: String = ""

    //INIT BLOCK
    init {
        viewModelScope.launch {
            //is this an edit or add screen?
            if (courseId != -1) {
                launch {
                    try {
                        //the course we want to edit. We get the state once
                        val editCourse =
                            courseRepository.getCourseStream(courseId).filterNotNull().first()
                        //we update the ui to edit state
                        _uiState.update { currentState ->
                            currentState.copy(
                                courseName = editCourse.name,
                                isEdit = true,
                                canAdd = true,
                                dialogSwitch = ::dialogSwitch,
                                deleteWithWords = ::deleteCourseAndWords,
                                deleteWithoutWords = ::deleteCourseWithoutWords
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("AddCourseViewModel", e.message ?: "no value given")
                    }
                }
            }
            //we get the all used names
            try {
                courseRepository.getAllCourses().filterNotNull().collect { courses ->
                    usedNames = courses.map { it.name.lowercase(Locale.getDefault()) }
                }
            } catch (e: Exception) {
                Log.e("AddCourseViewModel", e.message ?: "no value given")
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
    private fun addCourse() {
        viewModelScope.launch {
            try {
                courseRepository.insert(CourseEntity(0, userInput.trim()))
            } catch (e: Exception) {
                Log.e("AddCourseViewModel", e.message ?: "no value given")
            }
        }
    }

    /**update the entered course in the database*/
    private fun updateCourse() {
        viewModelScope.launch {
            try {
                courseRepository.update(CourseEntity(courseId, userInput.trim()))
            } catch (e: Exception) {
                Log.e("AddCourseViewModel", e.message ?: "no value given")
            }
        }
    }

    /**submit changes and either add a course or update an existing one*/
    fun submitChanges() {
        if (courseId != -1) {
            updateCourse()
        } else {
            addCourse()
        }
    }

    /**can we add a course?*/
    fun canAdd(): Boolean =
        userInput.isNotEmpty() && !usedNames.contains(userInput)

    //DELETE functions
    /**delete unit and words in it*/
    fun deleteCourseAndWords() {
        viewModelScope.launch {
            try {
                courseRepository.deleteCourseAndWords(courseId)
            } catch (e: Exception) {
                Log.e("AddCourseViewModel", e.message ?: "no message given")
            }
        }
    }

    /**delete unit and words in it*/
    fun deleteCourseWithoutWords() {
        viewModelScope.launch {
            try {
                courseRepository.deleteCourseWithoutWords(courseId)
            } catch (e: Exception) {
                Log.e("AddCourseViewModel", e.message ?: "no message given")
            }
        }
    }

    /**switch the dialog state between visible and not*/
    fun dialogSwitch() {
        val visibility = _uiState.value.deleteDialog
        _uiState.update { currentState ->
            currentState.copy(deleteDialog = !visibility)
        }
    }
}

data class AddCourseUiState(
    val courseName: String = "",
    val canAdd: Boolean = false,
    val isEdit: Boolean = false,
    //delete dialog data
    val deleteWithWords: () -> Unit = {},
    val deleteWithoutWords: () -> Unit = {},
    val dialogSwitch: () -> Unit = {},
    val deleteDialog: Boolean = false
)