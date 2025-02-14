package com.example.learner.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learner.R
import com.example.learner.classes.Course
import com.example.learner.data.testCourse
import com.example.learner.data.testCourses
import com.example.learner.ui.viewModels.CoursesUiState
import com.example.learner.ui.viewModels.CoursesViewModel

@Composable
fun CoursesScreen(
    toAddCourse: (Int) -> Unit,
    coursesViewModel: CoursesViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    //we get the ui state
    val uiState by coursesViewModel.uiState.collectAsState()

    //we encapsulate the body of the screen in order to preview it
    CourseScreenBody(uiState, toAddCourse) { course -> coursesViewModel.switchCourse(course) }
}

/**this function assembles ui from given ui state*/
@Composable
fun CourseScreenBody(
    uiState: CoursesUiState,
    toAddCourse: (Int) -> Unit,
    onCheck: (Course) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
            //HEADER
            Text(
                text = "Available courses:",
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
            //COURSES LIST:
            LazyColumn {
                items(uiState.courses.filter { it.cid != 1 }) { course ->
                    Row(modifier = Modifier.padding(dimensionResource(R.dimen.padding_tiny))) {
                        //each course is encapsulated in a card composable
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(
                                topStartPercent = 50,
                                topEndPercent = 50,
                                bottomStartPercent = 50,
                                bottomEndPercent = 50
                            )
                        ) {
                            //in a row we display the name, status and progress of a course
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = dimensionResource(R.dimen.padding_small),
                                        bottom = dimensionResource(R.dimen.padding_small),
                                        end = dimensionResource(R.dimen.padding_small),
                                        start = dimensionResource(R.dimen.padding_medium)
                                    )
                            ) {

                                Text(
                                    text = course.name,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { toAddCourse(course.cid) })
                                Row(
                                    modifier = Modifier.width(90.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Switch(
                                        checked = course == uiState.currentCourse,
                                        onCheckedChange = {
                                            onCheck(course)
                                        }
                                    )
                                    CircularProgressIndicator(
                                        progress = { course.getProgress() },
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                end = dimensionResource(R.dimen.padding_big),
                start = dimensionResource(R.dimen.padding_big),
                bottom = dimensionResource(R.dimen.padding_big),
                top = dimensionResource(R.dimen.padding_tiny)
            )
    ) {
        FloatingActionButton(
            onClick = { toAddCourse(-1) },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }
}

@Preview
@Composable
fun CoursesScreenPreview() {
    CourseScreenBody(CoursesUiState(testCourse, testCourses), {}) { }
}