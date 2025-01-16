package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.Course
import com.example.learner.data.Catalogue
import com.example.learner.data.course.CourseRepository
import com.example.learner.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**app view model determines the main screen and transitions from one screen to the next*/
class MainScrViewModel(userRepository: UserRepository, courseRepository: CourseRepository) :
    ViewModel() {
    //ui state is stored privately the user can only read the public value
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    /**chosen course*/
    private var currentCourse: Course = Catalogue.emptyCourse

    /**xp gained*/
    private var xp: Int = 0

    //INIT BLOCK - the state is collected and the ui is updated in a coroutine
    init {
        viewModelScope.launch {
            try {
                //we collect the xp state of the user and update UI
                launch {
                    userRepository.getUserData().filterNotNull().collect { userState ->
                        xp = userState.xp
                        updateUi()
                    }
                }
                //we collect the current course state and update UI
                courseRepository.getCurrentCourse().filterNotNull().collect { courseWithUW ->
                    currentCourse = courseWithUW.toCourse()
                    updateUi()
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", e.message ?: "no message given")
            }
        }
    }

    /**update the ui in accordance with the state of private view model variables*/
    private fun updateUi() {
        _uiState.update { currentState ->
            currentState.copy(xp = xp, currentCourse = currentCourse)
        }
    }
}

/**this class stores all data needed for the ui of the main screen*/
data class MainScreenUiState(
    val currentCourse: Course = Catalogue.emptyCourse,
    val xp: Int = 0,
    var openDialog: Boolean = false
)