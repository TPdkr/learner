package com.example.learner.data

import com.example.learner.classes.Course
import com.example.learner.classes.CourseUnit
import com.example.learner.classes.Gender
import com.example.learner.classes.Lesson
import com.example.learner.classes.Plural
import com.example.learner.classes.TaskType
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
    Word(german = "Dauer", translation = "duration", gender = Gender.DIE, plural = Plural.N),
    Word(
        german = "Schulweg",
        translation = "trip to school",
        gender = Gender.DER,
        plural = Plural.E
    ),
    Word(
        german = "Fremdsprache",
        translation = "foreign language",
        gender = Gender.DIE,
        plural = Plural.N
    ),
    Word(german = "laut", translation = "loud"),
    Word(german = "langweilig", translation = "boring"),
    Word(german = "lachen", translation = "to laugh"),
    Word(german = "very long word in german", translation = "very long translation")
)
val testTasks: List<Pair<Word, TaskType>> =
    testWords.map { Pair(it, TaskType.TYPE_TEXT) }
val testInfoTasks = testWords.shuffled().take(3).map { Pair(it, TaskType.INFO) }
val testLesson: Lesson = Lesson(tasks = testTasks)
val infoTestLesson: Lesson = Lesson(tasks = testInfoTasks)
val testUnit: CourseUnit = CourseUnit(testWords, "Test", 1, "test description")

val testCourse: Course = Course(List(1) { testUnit }, "test course try")

val testCourses: List<Course> =
    listOf(Course(listOf(), "Netzwerk A2.1"), Course(listOf(), "Netzwerk A2.2"), testCourse)