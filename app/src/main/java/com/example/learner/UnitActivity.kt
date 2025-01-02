package com.example.learner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.learner.data.testCourse
import com.example.learner.ui.UnitScreen
import com.example.learner.ui.theme.LearnerTheme

class UnitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UnitScreen(testCourse)
                }
            }
        }
    }
}