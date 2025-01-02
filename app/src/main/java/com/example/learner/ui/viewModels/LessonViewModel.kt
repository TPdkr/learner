package com.example.learner.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.learner.classes.Gender
import com.example.learner.classes.Lesson
import com.example.learner.classes.Plural
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

    //User guess variables
    var userGuess by mutableStateOf("")
        private set
    var userGenderGuess by mutableIntStateOf(-1)
        private set
    var userPluralGuess by mutableIntStateOf(-1)
        private set

    //Start the lesson and set the default values
    private fun startLesson() {
        //Log.d("Lesson error", "Start lesson is called")
        if (currentLesson.words.isNotEmpty()) {
            currentWord = currentLesson.words.first()//will need to change with list
            _uiState.value = LessonUiState(
                currentTrans = currentWord.translation,
                isNoun = isNoun(currentWord),
                taskCount = currentLesson.words.size
            )
        }
    }

    private fun isNoun(word: Word): Boolean =
        (word.gender != Gender.NOT_SET && word.plural != Plural.NOT_SET)

    //update user guess after change
    fun updateUserGuess(newGuess: String) {
        userGuess = newGuess
    }

    fun updateGenderGuess(gender: Int) {
        userGenderGuess = gender
    }

    fun updatePluralGuess(plural: Int) {
        userPluralGuess = plural
    }

    //move to next task if possible
    fun nextTask() {
        if (_uiState.value.taskNumber < _uiState.value.taskCount - 1) {
            val nextTaskNumber = _uiState.value.taskNumber + 1
            currentWord = currentLesson.words[nextTaskNumber]//Will need to change with list
            _uiState.update { currentState ->
                currentState.copy(
                    isChecked = false,
                    isWrong = false,
                    isNoun = isNoun(currentWord),
                    taskNumber = nextTaskNumber,
                    currentTrans = currentWord.translation
                )
            }
            updateUserGuess("")
            updatePluralGuess(-1)
            updateGenderGuess(-1)
        } else {
            //need to handle the end of the lesson
        }
        //Log.d("Lesson error", currentWord.german)

    }

    //check the input
    fun checkAnswer() {
        val isCorrect = userGuess.equals(
            currentWord.german, ignoreCase = true
        ) && userGenderGuess == (currentWord.gender.code) && (userPluralGuess == (currentWord.plural.code))
        _uiState.update { currentState ->
            val newScore = if (isCorrect) currentState.score.plus(20) else currentState.score
            currentState.copy(score = newScore, isChecked = true, isWrong = !isCorrect)
        }
    }
}

data class LessonUiState(
    val isChecked: Boolean = false,
    val isWrong: Boolean = false,
    val isNoun: Boolean = false,
    val taskNumber: Int = 0,
    val taskCount: Int = 0,
    val score: Int = 0,
    val wordCount: Int = 0,
    val currentTrans: String = ""
)
