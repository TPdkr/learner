package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.api.translation.getTranslation
import com.example.learner.classes.Gender
import com.example.learner.classes.Plural
import com.example.learner.classes.Word
import com.example.learner.data.relations.unitwithwords.UnitWithWordsRepository
import com.example.learner.data.word.WordEntity
import com.example.learner.data.word.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddWordViewModel(
    private val wordRepository: WordRepository,
    private val unitWordRepository: UnitWithWordsRepository
) :
    ViewModel() {
    //the user can only read the ui state
    private val _uiState = MutableStateFlow(AddWordUiState())
    val uiState = _uiState.asStateFlow()

    //the current id of the word
    private val wordId: Int = AppData.wordId
    private var wordRev: Int = 0
    private var wordRevTime: Long = 0

    //the input fields states
    private var inpGerm = ""
    private var inpTrans: String = ""
    private var inpGend: Int = -1
    private var inpPl: Int = -1

    private var choiceId: Int = -1

    //SETTERS:
    fun onGermChange(germ: String) {
        inpGerm = germ
        _uiState.update { currentState ->
            currentState.copy(inpGerm = germ)
        }
    }

    fun onTransChange(tr: String) {
        inpTrans = tr
        _uiState.update { currentState ->
            currentState.copy(inpTrans = tr)
        }
    }

    fun onGendChange(gnd: Int) {
        inpGend = gnd
        _uiState.update { currentState ->
            currentState.copy(inpGend = inpGend, canInsert = canAdd())
        }
    }

    fun onPlChange(pl: Int) {
        inpPl = pl
        _uiState.update { currentState ->
            currentState.copy(inpPl = pl, canInsert = canAdd())
        }
    }

    /**switch the is noun state*/
    fun isNounSwitch() {
        if (uiState.value.isNoun) {
            inpGend = -1
            inpPl = -1
            _uiState.update { currentState ->
                currentState.copy(
                    inpGend = inpGend,
                    inpPl = inpPl,
                    isNoun = false,
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(isNoun = true)
            }
        }
        _uiState.update { currentState ->
            currentState.copy(canInsert = canAdd())
        }
    }

    /**choose the word from list and set input fields*/
    fun chooseWord(word: Word) {
        inpGerm = word.german
        inpTrans = word.translation
        inpGend = word.gender.code
        inpPl = word.plural.code
        val isNoun = word.isNoun()
        _uiState.update { currentState ->
            currentState.copy(
                inpPl = inpPl,
                inpGend = inpGend,
                inpGerm = inpGerm,
                inpTrans = inpTrans,
                isNoun = isNoun,
                isChosen = true,
                addAndEditExisting = ::insertAndUpdateExisting
            )
        }
        _uiState.update { currentState ->
            currentState.copy(canInsert = canAdd())
        }
        choiceId = word.wid
    }

    /**resets the state of the screen*/
    fun reset(){
        onTransChange("")
        onPlChange(-1)
        onGendChange(-1)
        onGermChange("")
        _uiState.update{currentState->
            currentState.copy(isChosen = false, canInsert = false)
        }
    }

    init {
        viewModelScope.launch {
            if (wordId != -1) {
                launch {
                    try {
                        val editWord = wordRepository.getWordStream(wordId).filterNotNull().first()
                        inpGerm = editWord.german
                        inpTrans = editWord.translation
                        inpGend = editWord.gender
                        inpPl = editWord.plural
                        wordRev = editWord.revision
                        wordRevTime = editWord.revisionTime
                        _uiState.update { currentState ->
                            currentState.copy(
                                inpPl = inpPl,
                                inpGerm = inpGerm,
                                inpTrans = inpTrans,
                                inpGend = inpGend,
                                canInsert = true,
                                isEdit = true,
                                isNoun = editWord.toWord().isNoun(),
                                deleteFromUnit = ::deleteFromUnit,
                                deleteAll = ::deleteAll,
                                dialogSwitch = ::dialogSwitch,
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("AddWordViewModel", e.message ?: "no message given")
                    }
                }
            }
            try {
                val words = wordRepository.getAllWords().filterNotNull().firstOrNull()
                    ?.map { it.toWord() }
                    ?: emptyList()
                _uiState.update { currentState ->
                    currentState.copy(
                        wordList = words,
                        translateWord = ::translate,
                        snackbarAction = ::snackHide,
                        reset = ::reset
                    )
                }
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**insert the word into the database*/
    fun insert() {
        viewModelScope.launch {
            try {
                //we insert into the general word repo
                val wid: Int =
                    wordRepository.insertWord(
                        WordEntity(
                            0,
                            inpGerm.trim(),
                            inpTrans.trim(),
                            inpGend,
                            inpPl
                        )
                    )
                        .toInt()
                //we add the word to unit now that we know its iD
                val currentUnit = AppData.unitUid
                if (wid != -1) {
                    unitWordRepository.addWordToUnit(wid, currentUnit)
                }
                //we want to display a word
                val addedWord = Word(
                    0,
                    inpGerm.trim(),
                    inpTrans.trim(),
                    gender = Gender.fromCode(inpGend),
                    plural = Plural.fromCode(inpPl)
                )
                snackShow("added: ${addedWord.toUiString()}")
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**insert an existing id into cross ref*/
    private fun insertById(wid: Int) {
        viewModelScope.launch {
            try {
                val currentUnit = AppData.unitUid
                if (wid != -1) {
                    unitWordRepository.addWordToUnit(wid, currentUnit)
                }
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**insert and update existing word into the unit*/
    fun insertAndUpdateExisting() {
        if (_uiState.value.isChosen && choiceId != -1) {
            insertById(choiceId)
            update(choiceId)
            val addedWord = Word(
                0,
                inpGerm.trim(),
                inpTrans,
                gender = Gender.fromCode(inpGend),
                plural = Plural.fromCode(inpPl)
            )
            snackShow("updated and added to unit: ${addedWord.toUiString()}")
        }
    }

    /**update the word in the database*/
    fun update(wid: Int) {
        viewModelScope.launch {
            try {
                val chosenWord = wordRepository.getWordStream(wordId).filterNotNull().first()
                wordRepository.updateWord(
                    WordEntity(
                        wid,
                        inpGerm.trim(),
                        inpTrans.trim(),
                        inpGend,
                        inpPl,
                        chosenWord.revision,
                        chosenWord.revisionTime
                    )
                )
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**submit changes to the database*/
    fun submitChanges() {
        if (_uiState.value.isEdit) {
            update(wordId)
        } else {
            insert()
        }
    }
    //DELETE dialog functions:
    /**delete word from unit*/
    fun deleteFromUnit() {
        viewModelScope.launch {
            try {
                unitWordRepository.removeWordFromUnit(wordId, AppData.unitUid)
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**delete word from the database*/
    fun deleteAll() {
        deleteFromUnit()
        viewModelScope.launch {
            try {
                wordRepository.deleteById(wordId)
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**switch the dialog state between visible and not*/
    fun dialogSwitch() {
        val visibility = _uiState.value.deleteDialog
        _uiState.update { currentState ->
            currentState.copy(deleteDialog = !visibility)
        }
    }

    /**switch the snackbar state between visible and not*/
    fun snackHide() {
        _uiState.update { currentState ->
            currentState.copy(snackbarVisible = false)
        }
    }

    private fun snackShow(text: String){
        _uiState.update { currentState ->
            currentState.copy(snackbarVisible = true, snackbarText = text)
        }
    }

    /**can we add the word into the database?*/
    fun canAdd(): Boolean {
        return if (uiState.value.isNoun) {
            inpGend != -1 && inpPl != -1 && inpGerm.isNotEmpty() && inpTrans.isNotEmpty()
        } else {
            inpGerm.isNotEmpty() && inpTrans.isNotEmpty()
        }
    }

    /**translate the word into english*/
    fun translate() {
        //we set the state to loading
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        //data is fetched from the api
        viewModelScope.launch {
            Log.d("AddWordViewModel", "asked for response")
            val translation = getTranslation(_uiState.value.inpGerm.trim())
            Log.d("AddWordViewModel", "got response $translation")

            //we check for error state
            if(translation.isNotEmpty()){
                onTransChange(translation)
            } else {
                snackShow("error occurred try later")
            }

            //we set the state to not loading
            _uiState.update { currentState ->
                currentState.copy(isLoading = false, canInsert = canAdd())
            }
        }
    }
}

data class AddWordUiState(
    var wordList: List<Word> = listOf(),
    var inpGerm: String = "",
    var inpTrans: String = "",
    var inpGend: Int = -1,
    var inpPl: Int = -1,
    var isNoun: Boolean = true,
    var canInsert: Boolean = false,
    var isEdit: Boolean = false,
    //snackbar data
    var snackbarVisible: Boolean = false,
    var snackbarText: String = "",
    var snackbarAction: () -> Unit = {},
    //delete dialog state
    var deleteFromUnit: () -> Unit = {},
    var deleteAll: () -> Unit = {},
    var dialogSwitch: () -> Unit = {},
    var addAndEditExisting: () -> Unit = {},
    var reset: () -> Unit = {},
    //translate a word the user entered
    var translateWord: () -> Unit = {},
    var isLoading: Boolean = false,
    var deleteDialog: Boolean = false,
    var isChosen: Boolean = false
)