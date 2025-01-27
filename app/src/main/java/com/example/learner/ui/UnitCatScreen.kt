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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
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
import com.example.learner.data.testCourse
import com.example.learner.ui.viewModels.UnitCatUiState
import com.example.learner.ui.viewModels.UnitCatViewModel

@Composable
fun UnitCatScreen(
    unitCatViewModel: UnitCatViewModel = viewModel(factory = ViewModelFactory.Factory),
    toAddUnit: (Int) -> Unit,
    toUnit: (CourseUnit) -> Unit
) {
    val courseUiState by unitCatViewModel.uiState.collectAsState()

    //we call the body of the screen
    UnitCatScreenBody(
        courseUiState,
        toAddUnit,
        toUnit
    )
}

/**The body of the screen that shows unit catalogue*/
@Composable
fun UnitCatScreenBody(
    unitCatUiState: UnitCatUiState,
    toAddUnit: (Int) -> Unit,
    toUnit: (CourseUnit) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    end = dimensionResource(R.dimen.padding_big),
                    start = dimensionResource(R.dimen.padding_big),
                    bottom = dimensionResource(R.dimen.padding_big),
                    top = dimensionResource(R.dimen.padding_tiny)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = unitCatUiState.courseName,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                )
            }
            LazyColumn(Modifier.fillMaxSize()) {
                items(unitCatUiState.units.chunked(2)) { pair ->
                    Row {
                        UnitCard(
                            pair[0],
                            {
                                toUnit(pair[0])
                            }, modifier = Modifier
                                .weight(1f)
                                .padding(
                                    top = dimensionResource(R.dimen.padding_tiny),
                                    end = dimensionResource(R.dimen.padding_tiny),
                                    start = dimensionResource(R.dimen.padding_tiny)
                                )
                        )
                        if (pair.size == 2) {
                            UnitCard(
                                pair[1],
                                {
                                    toUnit(pair[1])
                                }, modifier = Modifier
                                    .weight(1f)
                                    .padding(
                                        top = dimensionResource(R.dimen.padding_tiny),
                                        end = dimensionResource(R.dimen.padding_tiny)
                                    )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(
                                        top = dimensionResource(R.dimen.padding_tiny),
                                        end = dimensionResource(R.dimen.padding_tiny)
                                    )
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(
                    end = dimensionResource(R.dimen.padding_big),
                    start = dimensionResource(R.dimen.padding_big),
                    bottom = dimensionResource(R.dimen.padding_big),
                    top = dimensionResource(R.dimen.padding_tiny)
                )
        ) {
            FloatingActionButton(
                onClick = {
                    if (unitCatUiState.cid != 1) {
                        toAddUnit(-1)
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }
}

/**A single unit card that displays key unit info*/
@Composable
fun UnitCard(
    unit: CourseUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.height(70.dp), onClick = onClick) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(70.dp)
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            //Main course info
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(4f),
            ) {
                Text(
                    text = stringResource(R.string.unit_number, unit.number),
                    style = typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = unit.name)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                //how many words are in long term or memorized?
                CircularProgressIndicator(
                    progress = { unit.getProgress() },
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun UnitCatPreview() {
    UnitCatScreenBody(unitCatUiState = UnitCatUiState(testCourse.units, testCourse.name), {}) {}
}