package com.example.learner.data

import com.example.learner.classes.Lesson
import com.example.learner.classes.Word

/**
 *
 *
 * */
val testWords: List<Word> = listOf(
    Word(german = "Auto", translation = "Car"),
    Word(german = "Abschluss", translation = "Certificate of graduation"),
    Word(german = "Schüler", translation = "school student(m)"),
    Word(german = "Dauer", translation = "duration"),
    Word(german = "Schulweg", translation = "trip to school"),
    Word(german = "Fremdsprache", translation = "foreign langugae")
)

val testLesson: Lesson= Lesson(words = testWords.shuffled())

