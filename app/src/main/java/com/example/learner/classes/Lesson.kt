package com.example.learner.classes

/**
LESSON DATA CLASS
This is where information about a lesson is stored
*/
enum class TaskType{
    TYPE_TEXT,
    INFO
}

data class Lesson (
    val words: List<Word>,
    val tasks: List<Pair<Word,TaskType>>
    //val lesson_length: Int = words.size
)