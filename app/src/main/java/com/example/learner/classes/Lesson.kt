package com.example.learner.classes

const val LESSON_LENGTH=15

/*
LESSON DATA CLASS
This is where information about a lesson is stored
*/
data class Lesson (
    val words: List<Word>,
    val totalScore: Int = words.sumOf { it.score }
)