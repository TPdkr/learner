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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learner.ui.viewModels.LessonViewModel


@Preview
@Composable
fun LessonScreen(lessonViewModel: LessonViewModel=LessonViewModel()) {
    val lessonUiState by lessonViewModel.uiState.collectAsState()
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
        Text(text = "0/15", style = typography.bodyLarge, modifier = Modifier.padding(20.dp))
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
                Text(text = "XP: 0")
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
                    onValueChange = {lessonViewModel.updateUserGuess(it) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.surface,
                        unfocusedContainerColor = colorScheme.surface,
                        disabledContainerColor = colorScheme.surface,
                    ),
                    label = { Text(text = "enter translation") },
                    isError = false,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { }
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
        ) {
            Button(onClick = {}, modifier = Modifier.width(300.dp)) {
                Text(text = "Check")
            }
        }

    }
}