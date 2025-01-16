package com.example.learner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learner.classes.Lesson
import com.example.learner.ui.viewModels.LessonData

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
 * states. The possible states are stored in [ScreenSate]
 */
@Composable
fun LearnerApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = ScreenSate.MainScreen.name,
        modifier = Modifier.fillMaxSize()
    ) {
        //MAIN SCREEN==========================================================
        composable(route = ScreenSate.MainScreen.name) {
            MainScreen(
                toUnits = { navController.navigate(ScreenSate.UnitsScreen.name) },
                toCourses = { navController.navigate(ScreenSate.CoursesScreen.name) },
                toLesson = { lesson ->
                    LessonData.lesson = lesson
                    navController.navigate(ScreenSate.LessonScreen.name)
                }
            )
        }
        //UNITS COURSE SCREEN==================================================
        composable(route = ScreenSate.UnitsScreen.name) {
            UnitScreen { lesson: Lesson ->
                LessonData.lesson = lesson
                navController.navigate(ScreenSate.LessonScreen.name)
            }
        }
        //COURSE CATALOGUE SCREEN==============================================
        composable(route = ScreenSate.CoursesScreen.name) {
            CoursesScreen()
        }
        //LESSON SCREEN========================================================
        composable(
            route = ScreenSate.LessonScreen.name
        ) {
            LessonScreen(toPrevious = { navController.popBackStack() })
        }
    }
}