package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                isNoun = isNoun
            )
        }
        _uiState.update { currentState ->
            currentState.copy(canInsert = canAdd())
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
                                isNoun = editWord.toWord().isNoun()
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
                _uiState.update { currentState -> currentState.copy(wordList = words) }
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**insert the word into the database*/
    fun insert() {
        viewModelScope.launch {
            try {
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
                val currentUnit = AppData.unitUid
                if (wid != -1) {
                    unitWordRepository.addWordToUnit(wid, currentUnit)
                }
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**update the word in the database*/
    fun update() {
        viewModelScope.launch {
            try {
                wordRepository.updateWord(
                    WordEntity(
                        wordId,
                        inpGerm.trim(),
                        inpTrans.trim(),
                        inpGend,
                        inpPl,
                        wordRev,
                        wordRevTime
                    )
                )
            } catch (e: Exception) {
                Log.e("AddWordViewModel", e.message ?: "no message given")
            }
        }
    }

    /**submit changes to the database*/
    fun submitChanges(){
        if(wordId!=-1){
            update()
        } else {
            insert()
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
}

data class AddWordUiState(
    var wordList: List<Word> = listOf(),
    var inpGerm: String = "",
    var inpTrans: String = "",
    var inpGend: Int = -1,
    var inpPl: Int = -1,
    var isNoun: Boolean = true,
    var canInsert: Boolean = false,
    var isEdit: Boolean = false
)