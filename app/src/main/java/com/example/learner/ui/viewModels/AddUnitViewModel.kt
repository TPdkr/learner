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

class AddUnitViewModel(val unitRepository: UnitRepository, val userRepository: UserRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(AddUnitUiState())
    val uiState = _uiState.asStateFlow()

    //desired name of the course
    private var userInputName: String = ""

    private var userInputDesc: String = ""

    /**change the name with the user input*/
    fun onNameChange(newValue: String) {
        if (newValue.length <= 30) {
            userInputName = newValue
            _uiState.update { currentState ->
                currentState.copy(
                    unitName = userInputName,
                    canAdd = userInputName.isNotEmpty()
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
                    canAdd = userInputName.isNotEmpty()
                )
            }
        }
    }

    /**insert the entered course into the database*/
    fun addUnit() {
        viewModelScope.launch {
            try {
                val currentCourse =
                    userRepository.getUserData().filterNotNull().first().currentCourseId
                val unitCount = unitRepository.getUnitCount(currentCourse).filterNotNull().first()
                val newUnit = UnitEntity(0, userInputName, userInputDesc, unitCount+1, currentCourse)
                unitRepository.insert(newUnit)
            } catch(e: Exception){
                Log.e("AddUnitViewModel", e.message?:"no message given")
            }
        }
    }

    /**can we add a course?*/
    fun canAdd(): Boolean =
        userInputName.isNotEmpty()
}

data class AddUnitUiState(
    val unitName: String = "",
    val unitDesc: String = "",
    val canAdd: Boolean = false
)