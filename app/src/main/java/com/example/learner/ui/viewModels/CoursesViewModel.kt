package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.classes.Course
import com.example.learner.data.Catalogue
import com.example.learner.data.course.CourseRepository
import com.example.learner.data.user.UserRepository
import com.example.learner.ui.ViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**this is the view model that supports the course cataloged screen*/
class CoursesViewModel(coursesRepository: CourseRepository, userRepository: UserRepository) :
    ViewModel() {
    private val _uiState =
        MutableStateFlow(CoursesUiState(currentCourse = Catalogue.emptyCourse, listOf()))
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    private var currentCourse: Course? = null
    private var allCourses: List<Course>? = null

    init {
        viewModelScope.launch {
            val userEntity = userRepository.getUserData().filterNotNull().first()
            val currentCourseId = userEntity.currentCourseId
            allCourses =
                coursesRepository.getAllCoursesWithUnitsAndWords().filterNotNull().first()
                    .map { it.toCourse() }
            currentCourse =
                coursesRepository.getCourseWithUnitsAndWords(currentCourseId).filterNotNull()
                    .first().toCourse()

            _uiState.update{ currentState ->
                currentState.copy(
                    currentCourse=currentCourse?: Catalogue.emptyCourse,
                    courses = allCourses?: listOf()
                )
            }
        }
    }

    //ui state is saved as private and the user can only read


    /**we update the ui state to display new course chosen*/
    fun switchCourse() {
        _uiState.update { currentState ->
            currentState.copy(
                currentCourse = currentCourse ?: Catalogue.emptyCourse,
                courses = allCourses ?: listOf()
            )
        }
    }
}

/**stores the information about the page of courses*/
data class CoursesUiState(
    val currentCourse: Course,
    val courses: List<Course>
)