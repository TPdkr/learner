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
NOUN GENDER
This is a enum class used for storing a gender of a noun
 */
enum class Gender(val code: Int) {
    DER(0),
    DIE(1),
    DAS(2),
    NOT_SET(3)
}

/**
NOUN PLURAL FORM
The options for the plural form of a noun in german.
 */
enum class Plural(val code: Int) {
    NO_CHANGE(0),
    E(1),
    E_UMLAUT(2),
    S(3),
    ER_UMLAUT(4),
    EN(5),
    NOT_SET(6)
}

/**
WORD DATA CLASS
This stores the information of a word that can be a noun and not a noun. I decided on that as
inheritance with data classes in Kotlin is a bit fucked apparently.
 */
data class Word(
    //common word properties:
    val german: String,
    val translation: String,
    var mistakes: Int = 0,
    val status: Status = Status.NEW,
    //noun properties:
    val gender: Gender = Gender.NOT_SET,
    val plural: Plural = Plural.NOT_SET
)