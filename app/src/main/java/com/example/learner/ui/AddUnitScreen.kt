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
import com.example.learner.ui.viewModels.AddUnitUiState
import com.example.learner.ui.viewModels.AddUnitViewModel

@Composable
fun AddUnitScreen(
    addUnitViewModel: AddUnitViewModel = viewModel(factory = ViewModelFactory.Factory),
    toPrevious: () -> Unit
) {
    val uiState by addUnitViewModel.uiState.collectAsState()
    AddUnitBody(uiState, {
        if (addUnitViewModel.canAdd()) {
            addUnitViewModel.addUnit()
            toPrevious()
        }
    }, { addUnitViewModel.onNameChange(it) }) { addUnitViewModel.onDescChange(it) }
}

/**the body of the add unit screen*/
@Composable
fun AddUnitBody(
    uiState: AddUnitUiState,
    onClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onDescChange: (String) -> Unit
) {
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
                    .height(250.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //title
                    Text(
                        text = "add unit",
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        lineHeight = 40.sp,
                        textAlign = TextAlign.Center
                    )
                    //unit name
                    TextField(
                        value = uiState.unitName,
                        onValueChange = { onNameChange(it) },
                        singleLine = true,
                        label = { Text("unit name") },
                        keyboardActions = KeyboardActions(onDone = { onClick() }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.surface,
                            unfocusedContainerColor = colorScheme.surface,
                            disabledContainerColor = colorScheme.surface,
                        )
                    )
                    //unit description
                    TextField(
                        value = uiState.unitDesc,
                        onValueChange = { onDescChange(it) },
                        singleLine = true,
                        label = { Text("unit description") },
                        keyboardActions = KeyboardActions(onDone = { onClick() }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.surface,
                            unfocusedContainerColor = colorScheme.surface,
                            disabledContainerColor = colorScheme.surface,
                        )
                    )
                    //button to add
                    Button(onClick = onClick, enabled = uiState.canAdd) {
                        Text("add unit to course")
                    }
                }
            }

        }
    }
}

@Composable
@Preview
fun AddUnitPreview() {
    AddUnitBody(AddUnitUiState("test unit", "test description", true), {}, {}, {})
}