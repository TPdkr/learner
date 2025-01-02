package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learner.R
import com.example.learner.classes.Course
import com.example.learner.classes.CourseUnit
import com.example.learner.data.testCourse

@Composable
fun UnitScreen(course: Course) {
    val units = course.units.chunked(2)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = course.name,
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(Modifier.fillMaxSize()) {
                items(units) { pair ->
                    Row {
                        UnitCard(
                            pair[0],
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 8.dp, end = 8.dp, start = 8.dp)
                        )
                        if (pair.size == 2) {
                            UnitCard(
                                pair[1],
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

@Composable
fun UnitCard(unit: CourseUnit, modifier: Modifier = Modifier) {
    Card(modifier.height(70.dp)) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
            Column(
                modifier = Modifier.fillMaxSize().weight(4f),
            ) {
                Text(
                    text = stringResource(R.string.unit_number, unit.number),
                    style = typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = unit.name)
            }
            CircularProgressIndicator(progress = {0.8F}, modifier = Modifier.fillMaxSize().weight(1f))
        }
    }

}

@Preview
@Composable
fun UnitPreview() {
    UnitScreen(testCourse)
}