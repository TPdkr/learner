package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.learner.R
import com.example.learner.classes.CourseUnit
import com.example.learner.classes.Lesson
import com.example.learner.data.testCourse
import com.example.learner.data.testUnit
import com.example.learner.ui.viewModels.CourseUnitViewModel

@Composable
fun UnitScreen(courseUnitViewModel: CourseUnitViewModel, toLesson: (Lesson) -> Unit) {
    val courseUiState by courseUnitViewModel.uiState.collectAsState()
    val showUnit = remember { mutableStateOf(false) }
    Surface(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = courseUiState.courseName,
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                    )
                }
                LazyColumn(Modifier.fillMaxSize()) {
                    items(courseUiState.units.chunked(2)) { pair ->
                        Row {
                            UnitCard(
                                pair[0],
                                {
                                    showUnit.value = true
                                    courseUnitViewModel.chooseUnit(pair[0])
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 8.dp, end = 8.dp, start = 8.dp)
                            )
                            if (pair.size == 2) {
                                UnitCard(
                                    pair[1],
                                    {
                                        showUnit.value = true
                                        courseUnitViewModel.chooseUnit(pair[1])
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 8.dp, end = 8.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 8.dp, end = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    if (showUnit.value) {
        UnitDetailedCard(
            courseUnitViewModel.chosenUnit,
            onDismissRequest = { showUnit.value = false },
            toLesson = toLesson
        )
    }
}

@Composable
fun UnitCard(unit: CourseUnit, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.height(70.dp), onClick = onClick) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3f),
            ) {
                Text(
                    text = stringResource(R.string.unit_number, unit.number),
                    style = typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = unit.name)
            }

            CircularProgressIndicator(
                progress = { unit.getProgress() },
                modifier = Modifier
                    .size(50.dp)
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }

}

@Composable
fun UnitDetailedCard(unit: CourseUnit, onDismissRequest: () -> Unit, toLesson: (Lesson) -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.unit_number, unit.number),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Text(text = unit.name, style = typography.titleMedium)
                    Text(text = unit.desc)

                }
                Text(text = "Words in this unit:", fontWeight = FontWeight.Bold)
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(8f),
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        items(unit.words) { word ->
                            Row(
                                modifier = Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text=word.toUiString())
                            }
                            HorizontalDivider(thickness = 2.dp)
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                ) {
                    Button(onClick = {toLesson(unit.learnLesson())}, modifier = Modifier.width(130.dp)) {
                        Text(text = "learn", style = typography.bodyMedium)
                    }
                    OutlinedButton(onClick = {}, modifier = Modifier.width(130.dp), enabled = false) {
                        Text(text = "add", style = typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CardPreview() {
    Surface {
        UnitDetailedCard(testUnit,{},{})
    }
}

@Preview
@Composable
fun UnitPreview() {
    UnitScreen(CourseUnitViewModel(testCourse)){}
}