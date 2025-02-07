package com.example.learner.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learner.classes.CourseUnit
import com.example.learner.data.testUnit
import com.example.learner.data.unit.UnitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val LOG_TAG_UVM: String = "UnitViewModel"

class UnitViewModel(unitRepository: UnitRepository) : ViewModel() {
    init {
        viewModelScope.launch {
            try {
                unitRepository.getUnitWithWords(AppData.unitUid).filterNotNull().collect{ unitWithWords->
                    val unit = unitWithWords.toCourseUnit()
                    _uiState.update { currentState->
                        currentState.copy(unit=unit)
                    }
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG_UVM, e.message ?: "no message given")
            }
        }
    }

    //the ui state is only readable to the user
    private val _uiState = MutableStateFlow(UnitUiState())
    val uiState: StateFlow<UnitUiState> = _uiState.asStateFlow()
}

data class UnitUiState(
    val unit: CourseUnit = testUnit
)