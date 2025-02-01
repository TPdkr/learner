package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.R
import com.example.learner.classes.TaskType
import com.example.learner.ui.viewModels.LessonUiState
import com.example.learner.ui.viewModels.LessonViewModel

val endings = listOf("-", "e", "e:", "s", "er:", "en", "n")
val genders = listOf("Der", "Die", "Das")

@Composable
fun LessonScreen(
    lessonViewModel: LessonViewModel = viewModel(factory = ViewModelFactory.Factory),
    toPrevious: () -> Unit
) {
    val lessonUiState by lessonViewModel.uiState.collectAsState()
    var isSubmitted by remember { mutableStateOf(false) }

    LessonScreenBody(lessonUiState, isSubmitted, onSubmit = {
        lessonViewModel.saveLesson()
        lessonViewModel.getFinalMessage()
        isSubmitted = true
    }, toPrevious = toPrevious)
}

/**this is the body of the lesson screen*/
@Composable
fun LessonScreenBody(
    lessonUiState: LessonUiState,
    isSubmitted: Boolean,
    onSubmit: () -> Unit,
    toPrevious: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LessonProgressBar(lessonUiState)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding()
                    .padding(dimensionResource(R.dimen.padding_big)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //This is the progress in lesson displayed right above the task card
                StatusRow(lessonUiState)
                //This is the task card
                when (lessonUiState.currentTaskType) {
                    TaskType.TYPE_TEXT -> TypeTaskCard(
                        lessonUiState
                    )

                    TaskType.INFO -> InfoCard(lessonUiState)
                }
                //This is the button section that changes depending on context to either check or next
                ControlBlock(lessonUiState) {
                    onSubmit()
                }
            }
        }
    }
    //the final lesson message
    if (isSubmitted) {
        FinalDialog(lessonUiState.score, toPrevious = toPrevious, lessonUiState)
    }
}

/**This is a row that displays xp status of a lesson*/
@Composable
fun StatusRow(lessonUiState: LessonUiState) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.xp, lessonUiState.score),
            style = typography.bodyLarge
        )
    }
}

/**This is a card of a task that only displays the info about a word instead of challenging the
 * user*/
@Composable
fun InfoCard(uiState: LessonUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min=300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_big)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.info,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**This is a card of a typing task in a lesson. Here the user has to type out the word letter by
 * letter*/
@Composable
fun TypeTaskCard(
    lessonUiState: LessonUiState
) {
    //This is the task card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min=300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_big)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //the english word we need to translate
            Text(
                text = lessonUiState.currentTrans,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )
            //here we allow the user to choose the gender of the word
            if (lessonUiState.isNoun) {
                AnswerSegmentedButton(
                    genders,
                    { index -> lessonUiState.onGenderChange(index) },
                    lessonUiState.genderGuess,
                    lessonUiState.isGendCorrect
                )
            }
            //german translation field
            AnswerTextField(lessonUiState)
            //here the user chooses the plural form
            if (lessonUiState.isNoun) {
                AnswerSegmentedButton(
                    endings,
                    { index -> lessonUiState.onPlChange(index) },
                    lessonUiState.plGuess,
                    lessonUiState.isPlCorrect
                )
            }
        }
    }
}

/**answer text field*/
@Composable
fun AnswerTextField(lessonUiState: LessonUiState) {
    val textFieldColors = when (lessonUiState.isGermCorrect) {
        true -> OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorScheme.secondaryContainer,
            unfocusedContainerColor = colorScheme.secondaryContainer,
            disabledContainerColor = colorScheme.surface,
            focusedTextColor = colorScheme.onSecondaryContainer,
            unfocusedTextColor = colorScheme.onSecondaryContainer,
            cursorColor = colorScheme.onSecondaryContainer,
            errorCursorColor = colorScheme.error,
            focusedBorderColor = colorScheme.onSecondaryContainer
        )

        false -> OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorScheme.errorContainer,
            unfocusedContainerColor = colorScheme.errorContainer,
            disabledContainerColor = colorScheme.surface,
            focusedTextColor = colorScheme.onErrorContainer,
            unfocusedTextColor = colorScheme.onErrorContainer,
            cursorColor = colorScheme.onErrorContainer,
            errorCursorColor = colorScheme.error,
            focusedBorderColor = colorScheme.onErrorContainer,
        )

        else -> OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorScheme.surface,
            unfocusedContainerColor = colorScheme.surface,
            disabledContainerColor = colorScheme.surface,
        )
    }
    val labelColors = when (lessonUiState.isGermCorrect) {
        true -> colorScheme.onSecondaryContainer
        false -> colorScheme.onErrorContainer
        else -> colorScheme.onSurface
    }
    //german translation field
    OutlinedTextField(
        value = lessonUiState.currentGuess,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { lessonUiState.onGuessChange(it) },
        colors = textFieldColors,
        label = {
            when (lessonUiState.isGermCorrect) {
                null -> {
                    Text(
                        text = "enter translation",
                        color = labelColors
                    )
                }

                false -> {
                    Text(
                        text = "mistake was made",
                        color = labelColors
                    )
                }

                else -> {
                    Text(
                        text = "correct!",
                        color = labelColors
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { }//!!! complete the action
        )
    )
}

/**a segmented button that takes a  list of [options], what to do [onClick] and current [choice]*/
@Composable
fun AnswerSegmentedButton(
    options: List<String>,
    onClick: (Int) -> Unit,
    choice: Int,
    isCorrect: Boolean? = null
) {
    //we want the color scheme to change based on state
    val buttonColors = when (isCorrect) {
        true -> SegmentedButtonColors(
            activeContainerColor = colorScheme.secondary,
            activeContentColor = colorScheme.onSecondary,
            activeBorderColor = colorScheme.onSecondaryContainer,
            inactiveContainerColor = colorScheme.secondaryContainer,
            inactiveContentColor = colorScheme.onSecondaryContainer,
            inactiveBorderColor = colorScheme.onSecondaryContainer,
            //doesn't matter as not used
            disabledActiveContainerColor = Color.Transparent,
            disabledActiveContentColor = colorScheme.onSurfaceVariant,
            disabledActiveBorderColor = colorScheme.surfaceVariant,
            disabledInactiveContainerColor = Color.Transparent,
            disabledInactiveContentColor = colorScheme.onSurfaceVariant,
            disabledInactiveBorderColor = colorScheme.surfaceVariant
        )

        false -> SegmentedButtonColors(
            activeContainerColor = colorScheme.error,
            activeContentColor = colorScheme.onError,
            activeBorderColor = colorScheme.onErrorContainer,
            inactiveContainerColor = colorScheme.errorContainer,
            inactiveContentColor = colorScheme.onErrorContainer,
            inactiveBorderColor = colorScheme.onErrorContainer,
            //doesn't matter as not used
            disabledActiveContainerColor = Color.Transparent,
            disabledActiveContentColor = colorScheme.onSurfaceVariant,
            disabledActiveBorderColor = colorScheme.surfaceVariant,
            disabledInactiveContainerColor = Color.Transparent,
            disabledInactiveContentColor = colorScheme.onSurfaceVariant,
            disabledInactiveBorderColor = colorScheme.surfaceVariant
        )

        else -> SegmentedButtonDefaults.colors()
    }
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onClick(index) },
                selected = index == choice,
                label = { Text(label) }, colors = buttonColors
            )
        }
    }
}

/**section of the screen that controls the flow of the lesson and users traversal over it*/
@Composable
fun ControlBlock(
    lessonUiState: LessonUiState,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_big)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!lessonUiState.isChecked && lessonUiState.currentTaskType != TaskType.INFO) {
            //check the answer
            Button(
                onClick = lessonUiState.onCheckAnswer,
                modifier = Modifier.width(300.dp)
            ) {
                Text(text = "Check")
            }
        } else if (lessonUiState.taskNumber + 1 == lessonUiState.taskCount) {
            //submit the final score
            OutlinedButton(
                onClick = onSubmit,
                modifier = Modifier.width(300.dp)
            ) {
                Text(text = "Submit")
            }
        } else {
            //to next task
            OutlinedButton(
                onClick = lessonUiState.onNextTask,
                modifier = Modifier.width(300.dp)
            ) {
                Text(text = "Next")
            }
        }
    }
}

/**a progress bar that shows the state of the lesson*/
@Composable
fun LessonProgressBar(lessonUiState: LessonUiState) {
    val progress = if (lessonUiState.taskCount > 0) {
        if(lessonUiState.isChecked) {
            (lessonUiState.taskNumber + 1).toFloat() / (lessonUiState.taskCount).toFloat()
        } else {
            (lessonUiState.taskNumber).toFloat() / (lessonUiState.taskCount).toFloat()
        }
    } else {
        0f
    }
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
    )
}

/**this is the final dialog that shows the total xp earned during a lesson and a message*/
@Composable
fun FinalDialog(score: Int, toPrevious: () -> Unit, lessonUiState: LessonUiState) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_big))
            ) {
                //title
                Text(
                    text = "You finished a lesson!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                //encouraging message
                Text(
                    text = lessonUiState.finalMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center
                )
                //the score:
                Text(
                    text = stringResource(R.string.you_earned_xp, score),
                    textAlign = TextAlign.Center,
                    style = typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                Text(
                    stringResource(R.string.lesson_percentage, lessonUiState.finalScore),
                    style = typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                //return to previous screen
                OutlinedButton(onClick = toPrevious, modifier = Modifier.fillMaxWidth()) {
                    Text("return", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Preview
@Composable
fun LessonInfoPreview() {
    LessonScreenBody(
        LessonUiState(
            currentTrans = "Car",
            currentGuess = "Auto",
            isNoun = true,
            genderGuess = 2,
            plGuess = 0,
            isGermCorrect = false
        ),
        false,
        {},
        {})
}

@Preview
@Composable
fun FinalPreview() {
    FinalDialog(2, {}, LessonUiState(finalMessage = "I had expectations...", finalScore = 20))
}