package com.example.learner.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.learner.data.LearnerApplication
import com.example.learner.ui.viewModels.MainScrViewModel
import com.example.learner.ui.viewModels.UnitCatViewModel
import com.example.learner.ui.viewModels.CoursesViewModel
import com.example.learner.ui.viewModels.LessonViewModel
import com.example.learner.ui.viewModels.UnitViewModel

object ViewModelFactory {
    val Factory = viewModelFactory {
        initializer {
            MainScrViewModel(
                learnerApp().container.userRepository,
                learnerApp().container.courseRepository
            )
        }

        initializer {
            CoursesViewModel(
                learnerApp().container.courseRepository,
                learnerApp().container.userRepository
            )
        }

        initializer {
            UnitCatViewModel(
                learnerApp().container.courseRepository
            )
        }

        initializer {
            LessonViewModel(
                learnerApp().container.wordRepository,
                learnerApp().container.userRepository
            )
        }

        initializer {
            UnitViewModel(
                learnerApp().container.unitRepository
            )
        }
    }
}

fun CreationExtras.learnerApp(): LearnerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LearnerApplication)