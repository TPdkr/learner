package com.example.learner.data

import com.example.learner.classes.Course
import com.example.learner.classes.CourseUnit
import com.example.learner.classes.Gender
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
        plural = Plural.E_UMLAUT,
        revision = -1
    ),
    Word(
        german = "Schüler",
        translation = "school student(m)",
        gender = Gender.DER,
        plural = Plural.NO_CHANGE,
        revision = -1
    ),
    Word(german = "Dauer", translation = "duration", gender = Gender.DIE, plural = Plural.N),
    Word(
        german = "Schulweg",
        translation = "trip to school",
        gender = Gender.DER,
        plural = Plural.E,
        revision = -1
    ),
    Word(
        german = "Fremdsprache",
        translation = "foreign language",
        gender = Gender.DIE,
        plural = Plural.N,
        revision = -1
    ),
    Word(german = "laut", translation = "loud"),
    Word(german = "langweilig", translation = "boring"),
    Word(german = "lachen", translation = "to laugh"),
    Word(german = "very long word in german", translation = "very long translation")
)
val testUnit: CourseUnit = CourseUnit(testWords, "Test. Unit name", 1, "test description")

val testCourse: Course = Course(List(5) { testUnit }, "test course try")

val testCourses: List<Course> =
    listOf(Course(listOf(), "Netzwerk A2.1"), Course(listOf(), "Netzwerk A2.2"), testCourse)