package com.example.learner.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learner.classes.Lesson
import com.example.learner.ui.viewModels.LessonData
import com.example.learner.ui.viewModels.MainScrViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    appViewModel: MainScrViewModel = viewModel(factory = ViewModelFactory.Factory),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ScreenSate.MainScreen.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = ScreenSate.MainScreen.name) {
            Text("main screen to be readjusted")
            MainScreen(
                toUnits = { navController.navigate(ScreenSate.UnitsScreen.name) },
                toCourses = { navController.navigate(ScreenSate.CoursesScreen.name) },
                toLesson = { lesson ->
                    LessonData.lesson = lesson
                    navController.navigate(ScreenSate.LessonScreen.name)
                },
                mainScreenViewModel = appViewModel
            )
        }
        composable(route = ScreenSate.UnitsScreen.name) {
            UnitScreen { lesson: Lesson ->
                LessonData.lesson = lesson
                navController.navigate(ScreenSate.LessonScreen.name)
            }
        }
        composable(route = ScreenSate.CoursesScreen.name) {
            CoursesScreen()
        }
        composable(
            route = ScreenSate.LessonScreen.name,
            //arguments = listOf(navArgument("lesson") { type = NavType.StringType })
        ) {
            Log.d("toLesson", "inside the lesson composable thingy")
            LessonScreen(toPrevious = { navController.popBackStack() }) { inc: Int ->
                appViewModel.updateScore(inc)
            }

        }
    }
}