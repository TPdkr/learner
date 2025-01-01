package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.learner.ui.viewModels.LessonViewModel

@Preview
@Composable
fun LessonScreen(lessonViewModel: LessonViewModel = viewModel()) {
    val lessonUiState by lessonViewModel.uiState.collectAsState()
    var isSubmitted by remember { mutableStateOf(false) }
    Surface(modifier = Modifier.fillMaxSize()) {

    }
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
        //This is the progress in lesson
        Text(
            text = stringResource(
                R.string.task_number,
                lessonUiState.taskNumber+1,
                lessonUiState.taskCount
            ), style = typography.bodyLarge, modifier = Modifier.padding(20.dp)
        )
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
                    text = stringResource(R.string.xp, lessonUiState.score)
                )
                Text(
                    text = lessonUiState.currentTrans,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                )
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
                    label = { Text(text = "enter translation") },
                    isError = lessonUiState.isWrong,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { }//TODO!!!!!Sch
                    )
                )
            }
        }
        //This is the button section that changes depending on context to either check or next
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
        ) {
            if (!lessonUiState.isChecked) {
                Button(
                    onClick = { lessonViewModel.checkAnswer() },
                    modifier = Modifier.width(300.dp)
                ) {
                    Text(text = "Check")
                }
            } else if (lessonUiState.taskNumber+1==lessonUiState.taskCount){
                OutlinedButton(onClick = { isSubmitted=true}, modifier = Modifier.width(300.dp)) {
                    Text(text = "Submit")
                }
            }else {
                OutlinedButton(onClick = {lessonViewModel.nextTask()}, modifier = Modifier.width(300.dp)) {
                    Text(text = "Next")
                }
            }
        }
    }
    if (isSubmitted){
        FinalDialog(lessonUiState.score)
    }
}

@Composable
fun TaskCard() {

}

@Composable
fun FinalDialog(score: Int){
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
                )
                Text(
                    text = "Good boy ;)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
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
                )
            }
        }
    }
}