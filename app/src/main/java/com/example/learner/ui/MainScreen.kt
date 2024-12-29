package com.example.learner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.learner.ui.theme.LearnerTheme

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val openDialog = remember { mutableStateOf(false) }
    LearnerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Are you ready to learn German?",
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                val buttonModifier = Modifier
                    .width(300.dp)
                    .padding(8.dp)
                Button(onClick = { print(6) }, modifier = buttonModifier) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Text(text = "continue learning")
                }
                Button(onClick = { print(3) }, modifier = buttonModifier) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = null
                    )
                    Text(text = "lessons")
                }
                Button(
                    onClick = { openDialog.value = true },
                    modifier = buttonModifier
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null
                    )
                    Text(text = "about")
                }
                Text(
                    text = "made by TPdkr",
                    textAlign = TextAlign.Center,
                    style = typography.labelMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (openDialog.value) {
                InfoDialog(onDismissRequest = { openDialog.value = false })
            }
        }
    }
}

@Composable
fun InfoDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Tanks for testing!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                )
                Text(
                    text = "This app is still in early development. Please report errors and bugs",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


val dummyFunction: () -> Unit = fun() {
    println("This is a dummy function.")
}


