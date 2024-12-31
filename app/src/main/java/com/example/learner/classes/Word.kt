package com.example.learner.classes

/**
WORD STATUS
A word can be either new, learning or in long term memory. The final stage is memorized.
*/
enum class Status(val code: Int) {
    NEW(0),
    LEARNING(1),
    LONG_TERM(2),
    MEMORIZED(3)
}

/**
WORD DATA CLASS
This stores the information of a word
*/
data class Word(
    val german: String,
    val translation: String,
    val score: Int=0,
    val status: Status = Status.NEW
)