package com.example.learner.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.Word
import com.example.learner.data.relations.unitwithwords.UnitWithWordsRepository
import com.example.learner.data.word.WordEntity
import com.example.learner.data.word.WordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddWordViewModel(
    private val wordRepository: WordRepository,
    val repository: UnitWithWordsRepository
) :
    ViewModel() {
    //the user can only read the ui state
    private val _uiState = MutableStateFlow(AddWordUiState())
    val uiState = _uiState.asStateFlow()

    private var inpGerm = ""
    private var inpTrans: String = ""
    private var inpGend: Int = -1
    private var inpPl: Int = -1

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

    fun isNounSwitch() {
        if (uiState.value.isNoun) {
            inpGend = -1
            inpPl = -1
            _uiState.update { currentState ->
                currentState.copy(
                    inpGend = inpGend,
                    inpPl = inpPl,
                    isNoun = false,
                    canInsert = canAdd()
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(isNoun = true, canInsert = canAdd())
            }
        }
    }

    fun insert() {
        viewModelScope.launch {
            wordRepository.insertWord(WordEntity(0, inpGerm, inpTrans, inpGend, inpPl))
        }
    }

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
)