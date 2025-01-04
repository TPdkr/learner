package com.example.learner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learner.classes.Course
import com.example.learner.classes.Lesson
import com.example.learner.ui.viewModels.AppViewModel
import com.example.learner.ui.viewModels.CourseUnitViewModel
import com.example.learner.ui.viewModels.CoursesViewModel
import com.example.learner.ui.viewModels.LessonViewModel

/**
 * ScreenState is a enum class that captures the state of the screen at any time
 */
enum class ScreenSate {
    MainScreen,
    LessonScreen,
    UnitsScreen,
    CoursesScreen
}

/**
 * This is the main function that is the entry into the app. It Navigates between different screen
 * states.
 */
@Composable
fun LearnerApp(
    appViewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    //the ui state of the app
    //val appUiState by appViewModel.uiState.collectAsState()
    // Get current back stack entry
    //val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    /*val currentScreen = ScreenSate.valueOf(
        backStackEntry?.destination?.route ?: ScreenSate.MainScreen.name
    )*/
    NavHost(
        navController = navController,
        startDestination = ScreenSate.MainScreen.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = ScreenSate.MainScreen.name) {
            MainScreen(
                toUnits = { navController.navigate(ScreenSate.UnitsScreen.name) },
                toCourses = { navController.navigate(ScreenSate.CoursesScreen.name) },
                toLesson = {
                    appViewModel.changeLesson(appViewModel.currentCourse.learnLesson())
                    navController.navigate(ScreenSate.LessonScreen.name)
                },
                toPrevious = { navController.popBackStack() },
                toReview = {
                    appViewModel.changeLesson(appViewModel.currentCourse.reviewLesson())
                    navController.navigate(ScreenSate.LessonScreen.name)
                },
                canReview = appViewModel.currentCourse.canReview(),
                canLearn = appViewModel.currentCourse.canLearn()

            )
        }
        composable(route = ScreenSate.UnitsScreen.name) {
            UnitScreen(
                CourseUnitViewModel(appViewModel.currentCourse)
            ) { lesson: Lesson ->
                appViewModel.changeLesson(lesson)
                navController.navigate(ScreenSate.LessonScreen.name)
            }
        }
        composable(route = ScreenSate.CoursesScreen.name) {
            CoursesScreen(
                chooseCourse = { course: Course -> appViewModel.switchCourse(course) },
                coursesViewModel = CoursesViewModel(appViewModel)
            )
        }
        composable(route = ScreenSate.LessonScreen.name) {
            LessonScreen(LessonViewModel(appViewModel.currentLesson)) { navController.popBackStack() }
        }
    }
}