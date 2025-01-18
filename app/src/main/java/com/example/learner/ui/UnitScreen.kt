package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
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
    toLesson: (Lesson) -> Unit
) {
    val uiSate by unitViewModel.uiState.collectAsState()
    val unit = uiSate.unit
    UnitScreenBody(unit, toLesson)
}

@Composable
fun UnitScreenBody(unit: CourseUnit, toLesson: (Lesson) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(10.dp)
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
                        .padding(10.dp)
                ) {
                    items(unit.words) { word ->
                        Row(
                            modifier = Modifier.padding(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = word.toUiString())
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
                    onClick = {},
                    modifier = Modifier.width(130.dp),
                    enabled = false
                ) {
                    Text(text = "add", style = typography.bodyMedium)
                }
            }
        }
    }
}

@Preview
@Composable
fun UnitScreenPreview() {
    LearnerTheme {
        UnitScreenBody(testUnit) { }
    }
}