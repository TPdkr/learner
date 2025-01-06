package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.learner.R
import com.example.learner.ui.theme.LearnerTheme

@Preview(showBackground = true)
@Composable
fun MainScreen(
    toUnits: () -> Unit = {},
    toCourses: () -> Unit = {},
    toLesson: () -> Unit = {},
    toPrevious: () -> Unit = {},
    toReview: () -> Unit = {},
    canReview: Boolean = false,
    canLearn: Boolean = false,
    reviewCount: Int = 0,
    xp: Int = 0
) {
    val openDialog = remember { mutableStateOf(false) }
    LearnerTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .safeDrawingPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //TITLE:
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.app_title),
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(stringResource(R.string.xp, xp),textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    //NAVIGATION:
                    Card {
                        MenuButton(
                            toReview,
                            stringResource(R.string.to_review_button, reviewCount),
                            Icons.Default.Refresh,
                            canReview
                        )
                        //this button starts a lesson test
                        MenuButton(toLesson,
                            stringResource(R.string.learn_words_button), Icons.Default.PlayArrow, canLearn)
                        //these are the units of current course
                        MenuButton(toUnits,
                            stringResource(R.string.units_button), Icons.Default.Menu)
                        MenuButton(toCourses,
                            stringResource(R.string.courses_catalogue_button), Icons.Default.Add)
                    }
                    Text(
                        text = "made by TPdkr",
                        textAlign = TextAlign.Center,
                        style = typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
                if (openDialog.value) {
                    InfoDialog(onDismissRequest = { openDialog.value = false })
                }
                //INFO DIALOG BUTTON
                TextButton(
                    onClick = { openDialog.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        //tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun MenuButton(onClick: () -> Unit, text: String, icon: ImageVector, enabled: Boolean = true) {
    val buttonModifier = Modifier
        .width(300.dp)
        .padding(8.dp)
    Button(onClick = onClick, modifier = buttonModifier, enabled = enabled) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Icon(
                icon,
                contentDescription = null
            )
            Text(text = text)
        }
    }
}

/**
 * This is a little info dialog that stores some information about the app.
 *  */
@Composable
fun InfoDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(5.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.tanks_for_testing),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                Text(
                    text = stringResource(R.string.info_app_message),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


