package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.data.unit.UnitEntity
import com.example.learner.data.unit.UnitRepository
import com.example.learner.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddUnitViewModel(
    private val unitRepository: UnitRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(AddUnitUiState())
    val uiState = _uiState.asStateFlow()

    //the unit id
    private var unitId = AppData.unitUid

    //the unit number
    private var unitNum = -1

    //desired name of the course
    private var userInputName: String = ""

    //desired description
    private var userInputDesc: String = ""

    init {
        if (unitId != -1) {
            viewModelScope.launch {
                try {
                    val editUnit = unitRepository.getUnitStream(unitId).filterNotNull().first()
                    unitNum = editUnit.number
                    userInputName = editUnit.name
                    userInputDesc = editUnit.desc
                    _uiState.update { currentState ->
                        currentState.copy(
                            unitName = editUnit.name,
                            unitDesc = editUnit.desc,
                            canAdd = true,
                            isEdit = true,
                            deleteWithWords = ::deleteUnitAndWords,
                            deleteWithoutWords = ::deleteUnitWithoutWords,
                            dialogSwitch = ::dialogSwitch

                        )
                    }
                } catch (e: Exception) {
                    Log.e("AddUnitViewModel", e.message ?: "no message given")
                }
            }
        }
    }

    /**change the name with the user input*/
    fun onNameChange(newValue: String) {
        if (newValue.length <= 30) {
            userInputName = newValue
            _uiState.update { currentState ->
                currentState.copy(
                    unitName = userInputName,
                    canAdd = canAdd()
                )
            }
        }
    }

    /**change the description*/
    fun onDescChange(newValue: String) {
        if (newValue.length <= 100) {
            userInputDesc = newValue
            _uiState.update { currentState ->
                currentState.copy(
                    unitDesc = userInputDesc,
                    canAdd = canAdd()
                )
            }
        }
    }

    /**insert the entered unit into the database*/
    private fun addUnit() {
        viewModelScope.launch {
            try {
                val currentCourse =
                    userRepository.getUserData().filterNotNull().first().currentCourseId
                val unitCount = unitRepository.getUnitCount(currentCourse).filterNotNull().first()
                val newUnit = UnitEntity(
                    0,
                    userInputName.trim(),
                    userInputDesc.trim(),
                    unitCount + 1,
                    currentCourse
                )
                unitRepository.insert(newUnit)
            } catch (e: Exception) {
                Log.e("AddUnitViewModel", e.message ?: "no message given")
            }
        }
    }

    /**update unit in the database*/
    private fun updateUnit() {
        viewModelScope.launch {
            try {
                val currentCourse =
                    userRepository.getUserData().filterNotNull().first().currentCourseId
                val editUnit = UnitEntity(
                    unitId,
                    userInputName.trim(),
                    userInputDesc.trim(),
                    unitNum,
                    currentCourse
                )
                unitRepository.update(editUnit)
            } catch (e: Exception) {
                Log.e("AddUnitViewModel", e.message ?: "no message given")
            }
        }
    }

    /**submit changes made by user*/
    fun submitChanges() {
        if (unitId != -1) {
            updateUnit()
        } else {
            addUnit()
        }
    }

    /**delete unit and words in it*/
    fun deleteUnitAndWords() {
        viewModelScope.launch {
            try {
                unitRepository.deleteUnitAndWords(unitId)
            } catch (e: Exception) {
                Log.e("AddUnitViewModel", e.message ?: "no message given")
            }
        }
    }

    /**delete unit and words in it*/
    fun deleteUnitWithoutWords() {
        viewModelScope.launch {
            try {
                unitRepository.deleteUnitWithoutWords(unitId)
            } catch (e: Exception) {
                Log.e("AddUnitViewModel", e.message ?: "no message given")
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

    /**can we add a course?*/
    fun canAdd(): Boolean =
        userInputName.isNotEmpty()
}

data class AddUnitUiState(
    val unitName: String = "",
    val unitDesc: String = "",
    val canAdd: Boolean = false,
    val isEdit: Boolean = false,
    //delete dialog options
    val deleteWithWords: () -> Unit = {},
    val deleteWithoutWords: () -> Unit = {},
    val dialogSwitch: () -> Unit = {},
    val deleteDialog: Boolean = false
)