package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        { addWordViewModel.isNounSwitch() },
        {
            if (addWordViewModel.canAdd()) {
                addWordViewModel.insert()
                toPrevious()
            }
        })

}

@Composable
fun AddWordBody(
    uiState: AddWordUiState,
    onPlCh: (Int) -> Unit,
    onGndCh: (Int) -> Unit,
    onTrCh: (String) -> Unit,
    onGermCh: (String) -> Unit,
    isNounSwitch: () -> Unit,
    submit: () -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(modifier = Modifier.height(400.dp)) {
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "",
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            lineHeight = 40.sp,
                            textAlign = TextAlign.Center
                        )
                        //the gender choice
                        if (uiState.isNoun) {//here we allow the user to choose the gender of the word
                            AnswerSegmentedButton(
                                genders,
                                { index -> onGndCh(index) },
                                uiState.inpGend
                            )
                        }
                        DropDownTextField(uiState, onGermCh)
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
                        if (uiState.isNoun) {//here the user chooses the plural form
                            AnswerSegmentedButton(
                                endings,
                                { index -> onPlCh(index) },
                                uiState.inpPl
                            )
                        }
                    }
                    Switch(checked = uiState.isNoun, { isNounSwitch() })
                }
            }
            Button(onClick = submit, enabled = uiState.canInsert) {
                Text(text = "add a new word")
            }
        }
    }
}

@Composable
fun DropDownTextField(uiState: AddWordUiState, onValueChange: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    expanded = uiState.wordList.isNotEmpty()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        TextField(
            value = uiState.inpGerm,
            onValueChange = {
                onValueChange(it)
                expanded = uiState.wordList.any { word ->
                    word.german.contains(
                        uiState.inpGerm,
                        ignoreCase = true
                    )
                }
            },
            label = {Text("german")},
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
            ),
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            scrollState = scrollState
        ) {
            LazyColumn {
                uiState.wordList.filter { it.german.contains(uiState.inpGerm, ignoreCase = true) }
            }
        }
        LaunchedEffect(expanded) {
            if (expanded) {
                // Scroll to show the bottom menu items.
                scrollState.scrollTo(scrollState.maxValue)
            }
        }
    }
}

@Preview
@Composable
fun AddWordPreview() {
    AddWordBody(AddWordUiState(), {}, {}, {}, {}, {}, {})
}