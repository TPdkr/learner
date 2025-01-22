package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.ui.viewModels.AddCourseUiState
import com.example.learner.ui.viewModels.AddCourseViewModel

@Composable
fun AddCourseScreen(
    returnBack: () -> Unit,
    addCourseViewModel: AddCourseViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val uiState by addCourseViewModel.uiState.collectAsState()
    AddCourseBody(uiState, {
        if (addCourseViewModel.canAdd()) {
            addCourseViewModel.addCourse()
            returnBack()
        }
    }, { addCourseViewModel.onValueChange(it) })
}

@Composable
fun AddCourseBody(uiState: AddCourseUiState, onClick: () -> Unit, onValueChange: (String) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add course",
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center
                    )
                    TextField(
                        value = uiState.courseName,
                        onValueChange = { onValueChange(it) },
                        singleLine = true,
                        keyboardActions = KeyboardActions(onDone = { onClick() }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.surface,
                            unfocusedContainerColor = colorScheme.surface,
                            disabledContainerColor = colorScheme.surface,
                        ),
                        label={Text("course name")}
                    )
                    Button(onClick = onClick, enabled = uiState.canAdd) {
                        Text("add new course")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddScreenPreview() {
    AddCourseBody(AddCourseUiState("new name...", true), {}, {})
}