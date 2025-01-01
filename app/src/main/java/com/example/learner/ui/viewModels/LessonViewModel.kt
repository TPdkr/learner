package com.example.learner.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.learner.classes.Lesson
import com.example.learner.classes.Word
import com.example.learner.data.testLesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LessonViewModel : ViewModel() {
    //Game ui state
    private val _uiState = MutableStateFlow(LessonUiState())
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    //this is the lesson data
    private val currentLesson: Lesson = testLesson

    //this is the current word
    private lateinit var currentWord: Word

    //we start the lesson
    init {
        startLesson()
    }

    var userGuess by mutableStateOf("")
        private set

    //Start the lesson and set the default values
    private fun startLesson() {
        Log.d("Lesson error", "Start lesson is called")
        if (currentLesson.words.isNotEmpty()) {
            currentWord = currentLesson.words.first()
            _uiState.value = LessonUiState(
                currentTrans = currentWord.translation,
                taskCount = currentLesson.words.size
            )
        }
    }

    //update user guess after change
    fun updateUserGuess(newGuess: String) {
        userGuess = newGuess
    }

    //move to next task if possible
    fun nextTask() {
        if (_uiState.value.taskNumber<_uiState.value.taskCount-1) {
            val nextTaskNumber=_uiState.value.taskNumber+1
            currentWord = currentLesson.words[nextTaskNumber]
            _uiState.update { currentState ->
                currentState.copy(
                    isChecked = false,
                    isWrong = false,
                    taskNumber = nextTaskNumber,
                    currentTrans = currentWord.translation
                )
            }
            updateUserGuess("")
        } else {
            //need to handle the end of the lesson
        }
        Log.d("Lesson error",currentWord.german)

    }

    //check the input
    fun checkAnswer() {
        val isCorrect = userGuess.equals(currentWord.german, ignoreCase = true)
        _uiState.update { currentState ->
            val newScore = if (isCorrect) currentState.score.plus(20) else currentState.score
            currentState.copy(score = newScore, isChecked = true, isWrong = !isCorrect)
        }
    }
}

data class LessonUiState(
    val isChecked: Boolean = false,
    val isWrong: Boolean = false,
    val taskNumber: Int = 0,
    val taskCount: Int = 0,
    val score: Int = 0,
    val wordCount: Int = 0,
    val currentTrans: String = ""
)
