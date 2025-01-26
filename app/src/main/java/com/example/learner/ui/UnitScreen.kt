package com.example.learner.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.R
import com.example.learner.classes.CourseUnit
import com.example.learner.classes.Lesson
import com.example.learner.data.testUnit
import com.example.learner.ui.theme.LearnerTheme
import com.example.learner.ui.viewModels.UnitViewModel

@Composable
fun UnitScreen(
    unitViewModel: UnitViewModel = viewModel(factory = ViewModelFactory.Factory),
    toAddWord: (Int) -> Unit,
    toLesson: (Lesson) -> Unit,
    toEditUnit: (Int) -> Unit
) {
    val uiSate by unitViewModel.uiState.collectAsState()
    val unit = uiSate.unit
    UnitScreenBody(unit, toAddWord, toLesson, toEditUnit)
}

@Composable
fun UnitScreenBody(
    unit: CourseUnit,
    toAddWord: (Int) -> Unit,
    toLesson: (Lesson) -> Unit,
    toEditUnit: (Int) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(dimensionResource(R.dimen.padding_big))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    items(unit.words) { word ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.padding_tiny)),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = word.toUiString(),
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { toAddWord(word.wid) }
                                    .weight(5f)
                            )
                            Text(
                                text = word.getRevisionTime(),
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                        HorizontalDivider(thickness = 2.dp)
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                Button(
                    onClick = { toLesson(unit.learnLesson()) },
                    modifier = Modifier.width(130.dp),
                    enabled = unit.canLearn()
                ) {
                    Text(text = "learn", style = typography.bodyMedium)
                }
                OutlinedButton(
                    onClick = { toAddWord(-1) },
                    modifier = Modifier.width(130.dp),
                ) {
                    Text(text = "add", style = typography.bodyMedium)
                }
            }
        }
        Box(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(dimensionResource(R.dimen.padding_big))
        ) {
            Icon(
                Icons.Filled.Create,
                contentDescription = "edit unit button",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { toEditUnit(unit.uid) }
            )
        }
    }
}

@Preview
@Composable
fun UnitScreenPreview() {
    LearnerTheme {
        UnitScreenBody(testUnit, {}, {}) { }
    }
}