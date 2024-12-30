package com.example.learner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.learner.ui.MainScreen
import com.example.learner.ui.theme.LearnerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnerTheme {
                MainScreen()
            }
        }
        /*val button: Button = findViewById(R.id.button_open_activity)
        button.setOnClickListener {
            // Launch SecondActivity
            val intent = Intent(this, LessonActivity::class.java)
            startActivity(intent)
        }*/
    }
}

