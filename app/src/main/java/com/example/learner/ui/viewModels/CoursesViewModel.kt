package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.classes.Course
import com.example.learner.data.Catalogue
import com.example.learner.data.course.CourseRepository
import com.example.learner.ui.ViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**this is the view model that supports the course cataloged screen*/
class CoursesViewModel(coursesRepository: CourseRepository) : ViewModel() {
    //ui state is saved as private and the user can only read
    private val _uiState =
        MutableStateFlow(CoursesUiState(appViewModel.currentCourse, Catalogue.courses))
    val uiState: StateFlow<CoursesUiState> = _uiState.asStateFlow()

    /**we update the ui state to display new course chosen*/
    fun switchCourse() {
        _uiState.update { currentState ->
            currentState.copy(
                currentCourse = appViewModel.currentCourse,
                courses = Catalogue.courses
            )
        }
    }
}

/**stores the information about the page of courses*/
data class CoursesUiState(
    val currentCourse: Course,
    val courses: List<Course>
)