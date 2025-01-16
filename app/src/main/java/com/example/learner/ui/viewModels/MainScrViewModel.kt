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
import kotlin.math.max

/**app view model determines the main screen and transitions from one screen to the next*/
class MainScrViewModel(userRepository: UserRepository, courseRepository: CourseRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    /**chosen course*/
    private var currentCourse: Course = Catalogue.emptyCourse
        private set

    /**xp gained*/
    private var xp: Int = 0

    init {
        viewModelScope.launch {
            try {
                //we collect the xp state of the user
                launch {
                    userRepository.getUserData().filterNotNull().collect { userState ->
                        xp = userState.xp
                        updateUi()
                    }
                }
                //we collect the current course state
                courseRepository.getCurrentCourse().filterNotNull().collect { courseWithUW ->
                    currentCourse = courseWithUW.toCourse()
                    updateUi()
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", e.message ?: "no message given")
            }
        }
    }

    private fun updateUi(){
        _uiState.update { currentState->
            currentState.copy(xp=xp, currentCourse = currentCourse)
        }
    }

    /**increment xp score of the user*/
    fun updateScore(inc: Int) {
        xp += max(a = 0, b = inc)
    }
}

data class MainScreenUiState(
    val currentCourse: Course=Catalogue.emptyCourse,
    val xp: Int=0
)