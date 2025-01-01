package com.example.learner.data

import com.example.learner.classes.Gender
import com.example.learner.classes.Lesson
import com.example.learner.classes.Plural
import com.example.learner.classes.Word

/**
 *
 *
 * */
val testWords: List<Word> = listOf(
    Word(german = "Auto", translation = "Car", gender = Gender.DAS, plural = Plural.S),
    Word(
        german = "Abschluss",
        translation = "Certificate of graduation",
        gender = Gender.DER,
        plural = Plural.E_UMLAUT
    ),
    Word(
        german = "Schüler",
        translation = "school student(m)",
        gender = Gender.DER,
        plural = Plural.NO_CHANGE
    ),
    Word(german = "Dauer", translation = "duration"),
    Word(german = "Schulweg", translation = "trip to school"),
    Word(german = "Fremdsprache", translation = "foreign langugae"),
    Word(german = "laut", translation = "loud"),
    Word(german = "langweilig", translation = "boring")
)

val testLesson: Lesson = Lesson(words = testWords.shuffled())

