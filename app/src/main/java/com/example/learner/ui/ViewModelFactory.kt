package com.example.learner.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.learner.data.LearnerApplication
import com.example.learner.ui.viewModels.MainScrViewModel
import com.example.learner.ui.viewModels.CourseUnitViewModel
import com.example.learner.ui.viewModels.CoursesViewModel
import com.example.learner.ui.viewModels.LessonViewModel

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
            CourseUnitViewModel(
                learnerApp().container.courseRepository,
                learnerApp().container.userRepository
            )
        }

        initializer {
            LessonViewModel(
                learnerApp().container.wordRepository,
                learnerApp().container.userRepository
            )
        }
    }
}

fun CreationExtras.learnerApp(): LearnerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as LearnerApplication)