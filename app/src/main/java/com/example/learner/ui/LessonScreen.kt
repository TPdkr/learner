package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.learner.data.infoTestLesson
import com.example.learner.data.testLesson
import com.example.learner.ui.viewModels.LessonUiState
import com.example.learner.ui.viewModels.LessonViewModel

val endings = listOf("-", "e", "e:", "s", "er:", "en", "n")
val genders = listOf("Der", "Die", "Das")

@Composable
fun LessonScreen(
    lessonViewModel: LessonViewModel = viewModel(factory = ViewModelFactory.Factory),
    toPrevious: () -> Unit,
    updateScore: (Int) -> Unit
) {
    val lessonUiState by lessonViewModel.uiState.collectAsState()
    var isSubmitted by remember { mutableStateOf(false) }
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //This is the progress in lesson displayed right above the task card
                StatusRow(lessonUiState)
                //This is the task card
                when (lessonUiState.currentTaskType) {
                    TaskType.TYPE_TEXT -> TypeTaskCard(lessonUiState, lessonViewModel)
                    TaskType.INFO -> InfoCard(lessonUiState)
                    //else -> Text(text = "error in task type info")
                }
                //This is the button section that changes depending on context to either check or next
                ControlBlock(lessonUiState, lessonViewModel) {
                    updateScore(lessonUiState.score)
                    lessonViewModel.saveLesson()
                    lessonViewModel.getFinalMessage()
                    isSubmitted = true
                }
            }
        }
    }

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
            .height(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
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
fun TypeTaskCard(lessonUiState: LessonUiState, lessonViewModel: LessonViewModel) {
    //This is the task card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = lessonUiState.currentTrans,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )
            if (lessonUiState.isNoun) {//here we allow the user to choose the gender of the word
                AnswerSegmentedButton(
                    genders,
                    { index -> lessonViewModel.updateGenderGuess(index) },
                    lessonViewModel.userGenderGuess
                )
            }
            OutlinedTextField(
                value = lessonViewModel.userGuess,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { lessonViewModel.updateUserGuess(it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                ),
                label = {
                    if (!lessonUiState.isWrong) Text(text = "enter translation") else Text(
                        text = "answer is wrong"
                    )
                },
                isError = lessonUiState.isWrong,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { }//TODO!!!!!Sch
                )
            )
            if (lessonUiState.isNoun) {//here the user chooses the plural form
                AnswerSegmentedButton(
                    endings,
                    { index -> lessonViewModel.updatePluralGuess(index) },
                    lessonViewModel.userPluralGuess
                )
            }
        }
    }
}

@Composable
        /**a segmented button that takes a  list of [options], what to do [onClick] and current [choice]*/
fun AnswerSegmentedButton(options: List<String>, onClick: (Int) -> Unit, choice: Int) {
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onClick(index) },
                selected = index == choice,
                label = { Text(label) }
            )
        }
    }
}

@Composable
        /**section of the screen that controls the flow of the lesson and users traversal over it*/
fun ControlBlock(
    lessonUiState: LessonUiState,
    lessonViewModel: LessonViewModel,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!lessonUiState.isChecked && lessonUiState.currentTaskType != TaskType.INFO) {
            Button(
                onClick = { lessonViewModel.checkAnswer() },
                modifier = Modifier.width(300.dp)
            ) {
                Text(text = "Check")
            }
        } else if (lessonUiState.taskNumber + 1 == lessonUiState.taskCount) {
            OutlinedButton(
                onClick = onSubmit,
                modifier = Modifier.width(300.dp)
            ) {
                Text(text = "Submit")
            }
        } else {
            OutlinedButton(
                onClick = { lessonViewModel.nextTask() },
                modifier = Modifier.width(300.dp)
            ) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
        /**a progress bar that shows the state of the lesson*/
fun LessonProgressBar(lessonUiState: LessonUiState) {
    LinearProgressIndicator(
        progress = {
            (lessonUiState.taskNumber).toFloat() / (lessonUiState.taskCount).toFloat()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    )
}

@Composable
        /**this is the final dialog that shows the total xp earned during a lesson and a message*/
fun FinalDialog(score: Int, toPrevious: () -> Unit, lessonUiState: LessonUiState) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(5.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "You finished a lesson!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(5.dp)
                )
                Text(
                    text = lessonUiState.finalMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.you_earned_xp, score),
                    textAlign = TextAlign.Center,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(10.dp)
                )
                TextButton(onClick = toPrevious, modifier = Modifier.fillMaxWidth()) {
                    Text("return back", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

/*@Preview
@Composable
fun LessonPreview() {
    LessonScreen(LessonViewModel(testLesson), {}) {}
}

@Preview
@Composable
fun LessonInfoPreview() {
    LessonScreen(LessonViewModel(infoTestLesson), {}) {}
}*/