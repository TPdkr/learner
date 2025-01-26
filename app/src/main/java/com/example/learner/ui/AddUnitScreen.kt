package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.R
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
            addUnitViewModel.submitChanges()
            toPrevious()
        }
    }, { addUnitViewModel.onNameChange(it) }, { addUnitViewModel.onDescChange(it) }, toPrevious)
}

/**the body of the add unit screen*/
@Composable
fun AddUnitBody(
    uiState: AddUnitUiState,
    submit: () -> Unit,
    onNameChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    toPrevious: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(dimensionResource(R.dimen.padding_big)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isEdit) {
                OutlinedButton(
                    { uiState.dialogSwitch() }, modifier = Modifier
                        .align(Alignment.End)
                        .padding(dimensionResource(R.dimen.padding_tiny))
                ) {
                    Icon(Icons.Default.Delete, "delete unit button")
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_big)),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //title
                    Text(
                        text = if (uiState.isEdit) "Edit unit" else "Add unit",
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
                        keyboardActions = KeyboardActions(onDone = { submit() }),
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
                        keyboardActions = KeyboardActions(onDone = { submit() }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorScheme.surface,
                            unfocusedContainerColor = colorScheme.surface,
                            disabledContainerColor = colorScheme.surface,
                        )
                    )
                    if (uiState.isEdit) {
                        //button to submit changes
                        OutlinedButton(onClick = submit, enabled = uiState.canAdd) {
                            Text("submit changes")
                        }
                    } else {
                        //button to add
                        Button(onClick = submit, enabled = uiState.canAdd) {
                            Text("add unit to course")
                        }
                    }
                }
            }

        }
        if (uiState.deleteDialog) {
            DeleteDialogUnit(
                onDismiss = { uiState.dialogSwitch() },
                deleteApp = {
                    uiState.deleteWithWords()
                    toPrevious()
                    toPrevious()
                },
                deleteLocal = {
                    uiState.deleteWithoutWords()
                    toPrevious()
                    toPrevious()
                }
            )
        }
    }
}

/**dialog if the user want to delete a unit from the course*/
@Preview
@Composable
fun DeleteDialogUnit(
    onDismiss: () -> Unit = {},
    deleteApp: () -> Unit = {},
    deleteLocal: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_big)),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Are you sure you want to delete unit?", style = typography.titleLarge)
                //delete all references unit and words in it
                OutlinedButton(deleteApp, Modifier.fillMaxWidth()) {
                    Text("delete with words")
                }
                //delete reference to unit but keep the words
                OutlinedButton(deleteLocal, Modifier.fillMaxWidth()) {
                    Text("delete without words")
                }
                //return back
                Button(onDismiss, Modifier.fillMaxWidth()) {
                    Text("return")
                }
            }
        }
    }
}

@Composable
@Preview
fun AddUnitPreview() {
    AddUnitBody(AddUnitUiState("test unit", "test description", true), {}, {}, {},{})
}

@Composable
@Preview
fun EditUnitPreview() {
    AddUnitBody(AddUnitUiState("test unit", "test description", canAdd = true, true), {}, {}, {},{})
}