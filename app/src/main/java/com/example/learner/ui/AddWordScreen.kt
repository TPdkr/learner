package com.example.learner.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.R
import com.example.learner.classes.Word
import com.example.learner.ui.viewModels.AddWordUiState
import com.example.learner.ui.viewModels.AddWordViewModel

@Composable
fun AddWordScreen(
    toPrevious: () -> Unit,
    addWordViewModel: AddWordViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val uiSate by addWordViewModel.uiState.collectAsState()
    AddWordBody(
        uiSate,
        { addWordViewModel.onPlChange(it) },
        { addWordViewModel.onGendChange(it) },
        { addWordViewModel.onTransChange(it) },
        { addWordViewModel.onGermChange(it) },
        { addWordViewModel.chooseWord(it) },
        { addWordViewModel.isNounSwitch() },
        {
            if (addWordViewModel.canAdd()) {
                addWordViewModel.submitChanges()
                toPrevious()
            }
        },
        toPrevious = toPrevious
    )

}

/**the body of add word screen*/
@Composable
fun AddWordBody(
    uiState: AddWordUiState,
    onPlCh: (Int) -> Unit,
    onGndCh: (Int) -> Unit,
    onTrCh: (String) -> Unit,
    onGermCh: (String) -> Unit,
    onChoice: (Word) -> Unit,
    isNounSwitch: () -> Unit,
    submit: () -> Unit,
    toPrevious: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(dimensionResource(R.dimen.padding_big))
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isEdit) {
                OutlinedButton(
                    { uiState.dialogSwitch() }, modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = dimensionResource(R.dimen.padding_tiny))
                ) {
                    Icon(Icons.Default.Delete, "delete word button")
                }
            }
            Card(modifier = Modifier.height(380.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_big))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //the title
                        Text(
                            text = if (uiState.isEdit) "Edit word" else "Add word",
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            lineHeight = 40.sp,
                            textAlign = TextAlign.Center
                        )
                        //the gender choice for nouns
                        if (uiState.isNoun) {
                            AnswerSegmentedButton(
                                genders,
                                { index -> onGndCh(index) },
                                uiState.inpGend
                            )
                        }
                        //this is the german language text field
                        DropDownTextField(uiState, onGermCh, onChoice)
                        //this is the translation text field
                        TextField(
                            value = uiState.inpTrans,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { onTrCh(it) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorScheme.surface,
                                unfocusedContainerColor = colorScheme.surface,
                                disabledContainerColor = colorScheme.surface,
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { submit() }
                            ),
                            label = { Text("translation") }
                        )
                        //here the user chooses the plural form for nouns
                        if (uiState.isNoun) {
                            AnswerSegmentedButton(
                                endings,
                                { index -> onPlCh(index) },
                                uiState.inpPl
                            )
                        }
                        if (uiState.isEdit) {
                            //submit the edit
                            OutlinedButton(onClick = submit, enabled = uiState.canInsert) {
                                Text(text = "submit changes")
                            }
                        } else {
                            Row(
                                horizontalArrangement = if (uiState.isChosen) Arrangement.SpaceBetween else Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                //this is the input button
                                Button(onClick = submit, enabled = uiState.canInsert) {
                                    Text(text = "add a new word")
                                }
                                if (uiState.isChosen) {
                                    OutlinedButton(onClick = {
                                        uiState.addAndEditExisting()
                                        toPrevious()
                                    }, enabled = uiState.canInsert) {
                                        Text(text = "use existing")
                                    }
                                }
                            }
                        }
                    }
                    //this is the noun state switch
                    Switch(
                        checked = uiState.isNoun, { isNounSwitch() }, modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    )
                }
            }
        }
        if (uiState.deleteDialog) {
            DeleteDialogWord(
                onDismiss = { uiState.dialogSwitch() },
                deleteApp = {
                    uiState.deleteAll()
                    toPrevious()
                },
                deleteLocal = {
                    uiState.deleteFromUnit()
                    toPrevious()
                }
            )
        }
    }
}

/**The drop down field in the word add screen*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownTextField(
    uiState: AddWordUiState,
    onValueChange: (String) -> Unit,
    onChoice: (Word) -> Unit
) {
    /**list of all words matching the input]*/
    val filteredList = uiState.wordList.filter {
        it.german.contains(uiState.inpGerm, ignoreCase = true)
    }

    /**is the drop down expanded?*/
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {
        //the text field associated with the drop down
        TextField(
            value = uiState.inpGerm,
            singleLine = true,
            onValueChange = {
                onValueChange(it)
                expanded = it.isNotEmpty() && filteredList.isNotEmpty()
                Log.d("add word screen", "value of german updates")
            },
            label = { Text("german") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
            ),
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
                .heightIn(max = 60.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
        )
        //the drop down list for the tet field
        if (filteredList.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier
                    .exposedDropdownSize(true)
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                properties = PopupProperties(focusable = false),
            ) {
                //this is the list of all word the user can choose from
                (filteredList.take(5)).forEach { word ->
                    //each word is displayed similar how it is displayed in unit screen
                    DropdownMenuItem(
                        onClick = {
                            Log.d("add word screen", "choice made")
                            expanded = false
                            onChoice(word)
                        },
                        text = { Text(word.toUiString()) }
                    )
                }
            }
        }
    }
}

/**dialog if the user want to delete a word from the unit*/
@Preview
@Composable
fun DeleteDialogWord(
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
                Text("Are you sure you want to delete word?", style = typography.titleLarge)
                //delete all references to word including in unit
                OutlinedButton(deleteApp, Modifier.fillMaxWidth()) {
                    Text("delete from app")
                }
                //delete reference to word in unit
                OutlinedButton(deleteLocal, Modifier.fillMaxWidth()) {
                    Text("delete from unit")
                }
                //return back
                Button(onDismiss, Modifier.fillMaxWidth()) {
                    Text("return")
                }
            }
        }
    }
}

@Preview
@Composable
fun AddWordPreview() {
    AddWordBody(AddWordUiState(canInsert = true), {}, {}, {}, {}, {}, {}, {}, {})
}

@Preview
@Composable
fun AddWordPreview2() {
    AddWordBody(AddWordUiState(canInsert = true, isChosen = true), {}, {}, {}, {}, {}, {}, {}, {})
}

@Preview
@Composable
fun EditWordPreview() {
    AddWordBody(AddWordUiState(isEdit = true, canInsert = true), {}, {}, {}, {}, {}, {}, {}, {})
}