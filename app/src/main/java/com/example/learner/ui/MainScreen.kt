package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.R
import com.example.learner.classes.Lesson
import com.example.learner.data.testCourse
import com.example.learner.ui.theme.LearnerTheme
import com.example.learner.ui.viewModels.MainScrViewModel
import com.example.learner.ui.viewModels.MainScreenUiState

@Composable
fun MainScreen(
    toUnits: () -> Unit = {},
    toCourses: () -> Unit = {},
    toLesson: (Lesson) -> Unit = {},
    mainScreenViewModel: MainScrViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val mainUiState = mainScreenViewModel.uiState.collectAsState().value

    MainScreenBody(
        toUnits,
        toCourses,
        toLesson,
        { mainScreenViewModel.infoDialogSwitch() },
        { mainScreenViewModel.buttonDialogSwitch() },
        { mainScreenViewModel.reset() },
        mainUiState
    )
}

/**Main screen body function assembles the UI based on UI state*/
@Composable
fun MainScreenBody(
    toUnits: () -> Unit = {},
    toCourses: () -> Unit = {},
    toLesson: (Lesson) -> Unit = {},
    infoDialogSwitch: () -> Unit = {},
    buttonDialogSwitch: () -> Unit = {},
    selfDestruct: () -> Unit = {},
    mainUiState: MainScreenUiState
) {
    LearnerTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .safeDrawingPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //TITLE:
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.app_title),
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            stringResource(R.string.xp, mainUiState.xp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            stringResource(R.string.words_learned_mainScr, mainUiState.wordCount),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    //NAVIGATION:
                    Column {
                        MenuButton(
                            { toLesson(mainUiState.currentCourse.reviewLesson()) },
                            stringResource(
                                R.string.to_review_button,
                                mainUiState.currentCourse.reviewCount()
                            ),
                            Icons.Default.Refresh,
                            mainUiState.currentCourse.canReview()
                        )
                        //this button starts a lesson test
                        MenuButton(
                            { toLesson(mainUiState.currentCourse.learnLesson()) },
                            stringResource(R.string.learn_words_button),
                            Icons.Default.PlayArrow,
                            mainUiState.currentCourse.canLearn()
                        )
                        //these are the units of current course
                        MenuButton(
                            toUnits,
                            stringResource(R.string.units_button), Icons.Default.Menu
                        )
                        MenuButton(
                            toCourses,
                            stringResource(R.string.courses_catalogue_button), Icons.Default.Add
                        )
                    }
                    //this references my Github profile
                    Text(
                        text = "made by TPdkr",
                        textAlign = TextAlign.Center,
                        style = typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_small))
                    )
                }
                //this checks if the dialog should be visible or not
                if (mainUiState.openDialog) {
                    InfoDialog(onDismissRequest = infoDialogSwitch)
                }
                if (mainUiState.openSelfDestruct) {
                    SelfDestructDialog(buttonDialogSwitch, selfDestruct)
                }
                //INFO DIALOG BUTTON
                TextButton(
                    onClick = infoDialogSwitch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                    )
                }
                //HIDDEN BUTTON
                TextButton(
                    onClick = buttonDialogSwitch,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.BottomStart)
                ) {
                    Text("  ")
                }
            }
        }
    }
}

/**A generic menu button composable that is used in the main screen*/
@Composable
fun MenuButton(onClick: () -> Unit, text: String, icon: ImageVector, enabled: Boolean = true) {
    val buttonModifier = Modifier
        .width(300.dp)
        .padding(dimensionResource(R.dimen.padding_tiny))
    Button(onClick = onClick, modifier = buttonModifier, enabled = enabled) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Icon(
                icon,
                contentDescription = null
            )
            Text(text = text)
        }
    }
}

/**
 * This is a little info dialog that stores some information about the app.
 *  */
@Preview
@Composable
fun InfoDialog(onDismissRequest: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            //The info message about the app is displayed
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_big))
            ) {
                Text(
                    text = stringResource(R.string.tanks_for_testing),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                Text(
                    text = stringResource(R.string.info_app_message),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**This is a self destruct button for the app data*/
@Preview
@Composable
fun SelfDestructDialog(onDismissRequest: () -> Unit = {}, onClick: () -> Unit = {}) {
    var isWarningVisible by rememberSaveable { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            //The info message about the app is displayed
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_big))
            ) {
                Text(
                    text = "You have found my self destruct button! It resets all user progress!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                ElevatedButton(
                    onClick = { isWarningVisible = true },
                    modifier = Modifier.size(150.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Default.Clear, "self destruct button")
                }
            }
        }
    }
    //the warning if user tries to delete all app data
    if (isWarningVisible) {
        SelfDestructWarning(
            onDismissRequest = {
                isWarningVisible = false
                onDismissRequest()
            },
            onDestruct = {
                isWarningVisible = false
                onClick()
                onDismissRequest()
            }
        )
    }
}

/**dialog asking if the user is sure about deleting all app data*/
@Preview
@Composable
fun SelfDestructWarning(onDismissRequest: () -> Unit = {}, onDestruct: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_big))
            ) {
                Text(
                    text = "Are you sure?",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("content will be deleted permanently")
                OutlinedButton(onDestruct, Modifier.fillMaxWidth()) {
                    Text("delete all app data")
                }
                Button(onDismissRequest, Modifier.fillMaxWidth()) {
                    Text("return")
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreenBody({}, {}, {}, {}, {}, {}, MainScreenUiState(testCourse, 45, 8, false))
}

