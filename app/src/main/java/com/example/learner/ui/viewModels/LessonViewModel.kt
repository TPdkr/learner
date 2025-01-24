package com.example.learner.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.CourseUnit
import com.example.learner.classes.Lesson
import com.example.learner.classes.TaskType
import com.example.learner.classes.Word
import com.example.learner.data.user.UserRepository
import com.example.learner.data.word.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object LessonData {
    var lesson: Lesson = Lesson(listOf())
    var unit: CourseUnit = CourseUnit(listOf(), "", 0, "")
    var unitUid: Int = 1
}

class LessonViewModel(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    //game ui state as state flow
    private val _uiState = MutableStateFlow(LessonUiState())
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    //this is the lesson data passed to the function
    private val currentLesson: Lesson = LessonData.lesson

    //this is the current word data
    private lateinit var currentWord: Word

    //we start the lesson in default state
    init {
        startLesson()
    }

    //user guess variables for inputs
    private var userGuess by mutableStateOf("")
    private var userGenderGuess by mutableIntStateOf(-1)
    private var userPluralGuess by mutableIntStateOf(-1)

    /**start the lesson and set the default values*/
    private fun startLesson() {
        if (currentLesson.tasks.isNotEmpty()) {
            currentWord = currentLesson.tasks.first().first//will need to change with list
            val newTaskType = currentLesson.tasks.first().second
            _uiState.value = LessonUiState(
                currentTrans = currentWord.translation,
                info = currentWord.toUiString(),
                currentTaskType = newTaskType,
                isNoun = isNoun(),
                taskCount = currentLesson.tasks.size,
                onCheckAnswer = ::checkAnswer,
                onNextTask = ::nextTask,
                onGenderChange = ::updateGenderGuess,
                onGuessChange = ::updateUserGuess,
                onPlChange = ::updatePluralGuess
            )
        }
    }

    /**is a given word a noun?*/
    private fun isNoun(): Boolean = currentWord.isNoun()

    /**update user guess after change*/
    fun updateUserGuess(newGuess: String) {
        userGuess = newGuess
        _uiState.update { currentState->
            currentState.copy(currentGuess = newGuess)
        }
    }

    /**update user gender guess for the word*/
    fun updateGenderGuess(gender: Int) {
        userGenderGuess = gender
        _uiState.update { currentState->
            currentState.copy(genderGuess = gender)
        }
    }

    /**update plural guess for the word*/
    fun updatePluralGuess(plural: Int) {
        userPluralGuess = plural
        _uiState.update { currentState->
            currentState.copy(plGuess = plural)
        }
    }

    /**save the state change after the lesson is over*/
    fun saveLesson() {
        saveScore()
        viewModelScope.launch {
            currentLesson.saveLesson(wordRepository = wordRepository)
        }
    }

    /**save the xp score of the lesson*/
    private fun saveScore() {
        viewModelScope.launch {
            val userData = userRepository.getUserData().filterNotNull().first()
            userData.xp += _uiState.value.score
            userRepository.update(userData)
        }
    }

    /**move to next task if possible*/
    fun nextTask() {
        if (_uiState.value.taskNumber < _uiState.value.taskCount - 1) {
            //we pre calculate the number of task
            val nextTaskNumber = _uiState.value.taskNumber + 1
            //update current word and task type
            currentWord = currentLesson.tasks[nextTaskNumber].first//Will need to change with list
            val newTaskType = currentLesson.tasks[nextTaskNumber].second
            //ui state is updated
            _uiState.update { currentState ->
                currentState.copy(
                    isChecked = false,
                    isWrong = false,
                    isNoun = isNoun(),
                    taskNumber = nextTaskNumber,
                    currentTrans = currentWord.translation,
                    info = currentWord.toUiString(),
                    currentTaskType = newTaskType
                )
            }
            //reset the user guess values
            updateUserGuess("")
            updatePluralGuess(-1)
            updateGenderGuess(-1)
        }
    }

    /**check the input from the user*/
    fun checkAnswer() {
        //is this answer correct?
        val isCorrect = currentWord.isCorrect(userGenderGuess, userGuess, userPluralGuess)
        if (!isCorrect) {
            currentWord.incMistakes()
        }
        currentWord.incRound()
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

    /**get user overall score on the lesson*/
    private fun getLessonScore(): Float =
        uiState.value.score.toFloat() / currentLesson.getMaxScore().toFloat()

    fun getFinalMessage() {
        val lessonScore = getLessonScore()
        val excellent = listOf(
            "good boy!(gender neutral)✨",
            "Slaying so hard, even Lady Gaga might be worried. 👑",
            "Du bist mein Lebkuchen",
            "I would hug you, but I'm just a text. I bet you give great hugs",
            "Main character energy right there",
            "You’re the blueprint, darling. Everyone else? Irrelevant. ✨"
        )
        val good = listOf(
            "This? This is the lukewarm tea of results. Sipable, but barely.",
            "I don't have anything to say. Either do better or do worse as know you are just mediocre",
            "I think for you it is a decent score",
            "You are giving almost there vibes I like it",
            "A round of applause! But like the slow slightly sarcastic kind...",
            "You have potential! Too bad it decided to take a nap"

        )
        val ok = listOf(
            "You could have done better",
            "And you call this a decent result??",
            "It's ok for memory to fade with time but not this fast",
            "Do better",
            "Mediocrity at its finest",
            "If I could sigh in text, I would"
        )
        val murder = listOf(
            "Pathetic",
            "Try cocaine, maybe that might help",
            "Can you allow me access to your location? No reason at all",
            "I would say I'm disappointed, but that would imply I had expectations",
            "You are making me want to uninstall myself",
            "Honey, even my error logs are cringing rn",
            "Are you doing this on purpose? If not, then it's just sad",
            "You’ve officially hit rock bottom. Congrats, I guess?"
        )
        val newInfo = when {
            lessonScore in 0.8..1.0 -> excellent.random()
            0.6 <= lessonScore && lessonScore < 0.8 -> good.random()
            0.45 <= lessonScore && lessonScore < 0.6 -> ok.random()
            else -> murder.random()
        }
        _uiState.update { currentSate ->
            currentSate.copy(finalMessage = newInfo)
        }
    }
}

/**These variables determine the state of the UI*/
data class LessonUiState(
    //the boolean values
    val isChecked: Boolean = false,
    val isWrong: Boolean = false,
    val isNoun: Boolean = false,
    //progress indicator
    val taskNumber: Int = 0,
    val taskCount: Int = 0,
    //xp count
    val score: Int = 0,
    val wordCount: Int = 0,
    //current user input and task data
    val currentTrans: String = "",
    val currentGuess: String = "",
    val genderGuess: Int = -1,
    val plGuess: Int = -1,
    //info card data
    val info: String = "",
    //final message data
    val finalMessage: String = "",
    val currentTaskType: TaskType = TaskType.TYPE_TEXT,
    //lambdas-------------------------------------------
    val onCheckAnswer: () -> Unit = {},
    val onNextTask: () -> Unit = {},
    //changing the answer
    val onGenderChange: (Int)->Unit = {},
    val onGuessChange: (String)->Unit={},
    val onPlChange: (Int)->Unit={}

)
