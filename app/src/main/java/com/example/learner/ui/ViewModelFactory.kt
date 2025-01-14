package com.example.learner.ui

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.learner.data.LearnerApp
import com.example.learner.ui.viewModels.AppViewModel
import com.example.learner.ui.viewModels.CourseUnitViewModel
import com.example.learner.ui.viewModels.CoursesViewModel

object ViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            AppViewModel()
        }

        initializer {
            CoursesViewModel(
                learnerApp().container.courseRepository
            )
        }

        initializer {
            CourseUnitViewModel(
                learnerApp().container.courseRepository,
                this.createSavedStateHandle()
            )
        }
    }
}

fun CreationExtras.learnerApp(): LearnerApp =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LearnerApp)