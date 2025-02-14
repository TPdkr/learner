package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.Course
import com.example.learner.data.Catalogue
import com.example.learner.data.course.CourseRepository
import com.example.learner.data.user.UserRepository
import com.example.learner.data.word.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**app view model determines the main screen and transitions from one screen to the next*/
class MainScrViewModel(
    private val selfDestruct: suspend () -> Unit,
    userRepository: UserRepository,
    courseRepository: CourseRepository,
    wordRepository: WordRepository
) :
    ViewModel() {
    //ui state is stored privately the user can only read the public value
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    /**chosen course*/
    private var currentCourse: Course = Catalogue.emptyCourse

    /**xp gained*/
    private var xp: Int = 0

    /**words learned*/
    private var wordCount: Int = 0

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
                //we collect current word count
                launch {
                    wordRepository.getDoneWordCount().filterNotNull().collect { wC ->
                        wordCount = wC
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
            currentState.copy(xp = xp, currentCourse = currentCourse, wordCount = wordCount)
        }
    }

    fun infoDialogSwitch() {
        val initOpenDialog = uiState.value.openDialog
        _uiState.update { currentState ->
            currentState.copy(openDialog = !initOpenDialog)
        }
    }

    fun buttonDialogSwitch() {
        val initOpenDialog = uiState.value.openSelfDestruct
        _uiState.update { currentState ->
            currentState.copy(openSelfDestruct = !initOpenDialog)
        }
    }

    fun reset() {
        viewModelScope.launch { selfDestruct() }
    }
}

/**this class stores all data needed for the ui of the main screen*/
data class MainScreenUiState(
    val currentCourse: Course = Catalogue.emptyCourse,
    val wordCount: Int = 0,
    val xp: Int = 0,
    var openDialog: Boolean = false,
    var openSelfDestruct: Boolean = false
)