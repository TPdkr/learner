package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.Course
import com.example.learner.data.Catalogue
import com.example.learner.data.course.CourseRepository
import com.example.learner.data.user.UserEntity
import com.example.learner.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**this is the view model that supports the course cataloged screen it receives [coursesRepository]
 *  and [userRepository] as input and interfaces with the database*/
class CoursesViewModel(
    private val coursesRepository: CourseRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {
    //ui state is saved as private and the user can only read
    private val _uiState =
        MutableStateFlow(CoursesUiState(currentCourse = Catalogue.emptyCourse, listOf()))
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    private var currentCourse: Course? = null
    private var allCourses: List<Course>? = null

    //INIT BLOCK - collects the state from the database in a coroutine
    init {
        viewModelScope.launch {
            try {
                //course catalogue is loaded in and updates with the database
                coursesRepository.getAllCoursesWithUnitsAndWords().filterNotNull()
                    .collect { courses ->
                        allCourses = courses.map { it.toCourse() }
                        //we load the current course and update UI
                        getCurrentCourse()
                        updateUI()
                    }
            } catch (e: Exception) {
                Log.e("CourseViewModel", e.message ?: "no message given")
            }
        }
    }

    /**get the current course*/
    private suspend fun getCurrentCourse() {
        try {
            currentCourse = coursesRepository.getCurrentCourse().filterNotNull()
                .first().toCourse()
        } catch (e: Exception) {
            Log.e("CourseViewModel", e.message ?: "no message given")
        }
    }

    /**this function updates the state of the Ui*/
    private fun updateUI() {
        _uiState.update { currentState ->
            currentState.copy(
                currentCourse = currentCourse ?: Catalogue.emptyCourse,
                courses = allCourses ?: listOf()
            )
        }
    }

    /**we update the ui state to display new course chosen*/
    fun switchCourse(course: Course) {
        viewModelScope.launch {
            try {
                //we switch the course in user data repo
                val userEntity = userRepository.getUserData().filterNotNull().first()
                val newUserEntity = UserEntity(userEntity.id, course.cid, userEntity.xp)
                userRepository.update(newUserEntity)
                //we need to make sure the ui state updates as well
                _uiState.update { currentState ->
                    currentState.copy(currentCourse = course)
                }
            } catch (e: Exception) {
                Log.e("CourseViewModel", e.message ?: "no message given")
            }
        }
    }
}

/**stores the information about the page of courses*/
data class CoursesUiState(
    val currentCourse: Course,
    val courses: List<Course>
)