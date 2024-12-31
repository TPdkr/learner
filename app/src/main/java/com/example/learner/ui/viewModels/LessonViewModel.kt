package com.example.learner.ui.viewModels

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
    init{
        startLesson()
    }
    var userGuess by mutableStateOf("")
        private set
    fun startLesson(){
        currentWord=currentLesson.words.first()
        _uiState.value=LessonUiState(currentTrans = currentWord.translation)
    }
    fun updateUserGuess(newGuess: String){
        userGuess=newGuess
    }
}

data class LessonUiState(
    val isChecked: Boolean = false,
    val taskNumber: Int = 0,
    val score: Int = 0,
    val wordCount: Int = 0,
    val currentTrans: String=""
)
