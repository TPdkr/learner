package com.example.learner.ui

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
                    val lessonJson = Json.encodeToString(lesson)
                    navController.navigate("${ScreenSate.LessonScreen.name}/$lessonJson")
                },
                mainScreenViewModel = appViewModel
            )
        }
        composable(route = ScreenSate.UnitsScreen.name) {
            UnitScreen { lesson: Lesson ->
                val lessonJson = Json.encodeToString(lesson)
                navController.navigate("${ScreenSate.LessonScreen.name}/$lessonJson")
            }
        }
        composable(route = ScreenSate.CoursesScreen.name) {
            CoursesScreen()
        }
        composable(
            route = "${ScreenSate.LessonScreen.name}/{lesson}",
            arguments = listOf(navArgument("lesson") { type = NavType.StringType })
        ) { backsStackEntry ->
            val lessonJson = backsStackEntry.arguments?.getString("lesson")
            val lesson = lessonJson?.let { Json.decodeFromString<Lesson>(it) }
            if (lesson != null) {
                LessonScreen(toPrevious = { navController.popBackStack() }) { inc: Int ->
                    appViewModel.updateScore(inc)
                }
            } else {
                Text("empty lesson passed")
            }
        }
    }
}