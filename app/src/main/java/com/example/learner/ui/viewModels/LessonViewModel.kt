package com.example.learner.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.learner.classes.Gender
import com.example.learner.classes.Lesson
import com.example.learner.classes.Plural
import com.example.learner.classes.TaskType
import com.example.learner.classes.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LessonViewModel(lesson: Lesson) : ViewModel() {
    //game ui state as state flow
    private val _uiState = MutableStateFlow(LessonUiState())
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    //this is the lesson data passed to the function
    private val currentLesson: Lesson = lesson

    //this is the current word data
    lateinit var currentWord: Word
        private set

    //we start the lesson in default state
    init {
        startLesson()
    }

    //user guess variables for inputs
    var userGuess by mutableStateOf("")
        private set
    var userGenderGuess by mutableIntStateOf(-1)
        private set
    var userPluralGuess by mutableIntStateOf(-1)
        private set

    //start the lesson and set the default values
    private fun startLesson() {
        if (currentLesson.tasks.isNotEmpty()) {
            currentWord = currentLesson.tasks.first().first//will need to change with list
            val newTaskType = currentLesson.tasks.first().second
            _uiState.value = LessonUiState(
                currentTrans = currentWord.translation,
                currentTaskType = newTaskType,
                isNoun = isNoun(currentWord),
                taskCount = currentLesson.tasks.size
            )
        }
    }

    //is a given word a noun?
    private fun isNoun(word: Word): Boolean = currentWord.isNoun()

    //update user guess after change
    fun updateUserGuess(newGuess: String) {
        userGuess = newGuess
    }

    //update user gender guess for the word
    fun updateGenderGuess(gender: Int) {
        userGenderGuess = gender
    }

    //update plural guess for the word
    fun updatePluralGuess(plural: Int) {
        userPluralGuess = plural
    }

    fun saveLesson(){
        currentLesson.saveLesson()
    }

    //move to next task if possible
    fun nextTask() {
        if (_uiState.value.taskNumber < _uiState.value.taskCount - 1) {
            val nextTaskNumber = _uiState.value.taskNumber + 1
            currentWord = currentLesson.tasks[nextTaskNumber].first//Will need to change with list
            val newTaskType = currentLesson.tasks[nextTaskNumber].second
            _uiState.update { currentState ->
                currentState.copy(
                    isChecked = false,
                    isWrong = false,
                    isNoun = isNoun(currentWord),
                    taskNumber = nextTaskNumber,
                    currentTrans = currentWord.translation,
                    currentTaskType = newTaskType
                )
            }
            updateUserGuess("")
            updatePluralGuess(-1)
            updateGenderGuess(-1)
        } else {
            //need to handle the end of the lesson
        }
    }

    //check the input
    fun checkAnswer() {
        //is this answer correct?
        val isCorrect = currentWord.isCorrect(userGenderGuess, userGuess, userPluralGuess)
        //update ui state
        val inc = currentWord.countScore(userGenderGuess, userGuess, userPluralGuess)
        _uiState.update { currentState ->
            val newScore = if (isCorrect) currentState.score.plus(inc) else currentState.score
            currentState.copy(score = newScore, isChecked = true, isWrong = !isCorrect)
        }
        //we want to show the correct answer
        updateUserGuess(currentWord.german)
        updateGenderGuess(currentWord.gender.code)
        updatePluralGuess(currentWord.plural.code)
    }
}

//These variables determine the state of the UI
data class LessonUiState(
    val isChecked: Boolean = false,
    val isWrong: Boolean = false,
    val isNoun: Boolean = false,
    val taskNumber: Int = 0,
    val taskCount: Int = 0,
    val score: Int = 0,
    val wordCount: Int = 0,
    val currentTrans: String = "",
    val currentTaskType: TaskType = TaskType.TYPE_TEXT
)
