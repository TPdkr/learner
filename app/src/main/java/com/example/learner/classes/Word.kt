package com.example.learner.classes

import androidx.compose.ui.util.fastJoinToString
import kotlin.math.max

/**
WORD STATUS
A word can be either new, learning or in long term memory. The final stage is memorized.
 */
enum class Status(val code: Int) {
    NEW(0),
    LEARNING(1),
    LONG_TERM(2),
    MEMORIZED(3),
    REVIEW(4)
}

/**
NOUN GENDER
This is a enum class used for storing a gender of a noun
 */
enum class Gender(val code: Int) {
    DER(0),
    DIE(1),
    DAS(2),
    NOT_SET(-1)
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
    N(6),
    NOT_SET(-1),

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
    //properties relevant for lesson contents:
    /**number of mistakes made during 1 lesson*/
    var mistakes: Int = 0,
    /**word status says whether it should be learned, reviewed or not*/
    val status: Status = Status.NEW,
    /**the time a word comes up in a given lesson*/
    var round: Int = 0,
    /**the time a word is revised, how many lessons it has been in?*/
    var revision: Int = 0,
    /**when should a word be revised?*/
    var revisionTime: Int = 0,//time value need to be set
    //noun properties:
    val gender: Gender = Gender.NOT_SET,
    val plural: Plural = Plural.NOT_SET
) {
    fun toUiString(): String {
        val genderStr = when (gender) {
            Gender.DER -> "Der"
            Gender.DIE -> "Die"
            Gender.DAS -> "Das"
            Gender.NOT_SET -> ""
        }
        val endingStr = when (plural) {
            Plural.NO_CHANGE -> "-"
            Plural.E -> "-e"
            Plural.E_UMLAUT -> "-e:"
            Plural.S -> "-s"
            Plural.ER_UMLAUT -> "-er:"
            Plural.EN -> "-en"
            Plural.N -> "-n"
            Plural.NOT_SET -> ""
        }
        val germanStr = (listOf(genderStr, german, endingStr).fastJoinToString(
            separator = " "
        ))
        val text = listOf(germanStr, translation).fastJoinToString(separator = " = ")
        return text
    }

    fun resetLesson() {
        mistakes = 0
        round = 0
        revision++
    }

    fun countScore(genderGuess: Int, germanGuess: String, pluralGuess: Int): Int {
        val isCorrect = isCorrect(genderGuess,germanGuess,pluralGuess)
        val isGenderCorrect = if (genderGuess == gender.code) 0F else 1F
        val isGermanCorrect = if (germanGuess == german) 0F else 1F
        val isPluralCorrect = if (pluralGuess == plural.code) 0F else 1F
        val guessScore = isPluralCorrect * 0.25 + isGenderCorrect * 0.25 + isGermanCorrect * 0.5
        val attemptMargin = if (isCorrect) round.toFloat() * 0.5F else 0F

        return max((20 - guessScore * 2 - attemptMargin).toInt(), 0)
    }

    fun isNoun(): Boolean = gender != Gender.NOT_SET && plural != Plural.NOT_SET

    fun isCorrect(genderGuess: Int, germanGuess: String, pluralGuess: Int): Boolean =
        germanGuess.equals(german, ignoreCase = true)
                && genderGuess == (gender.code) && (pluralGuess == (plural.code))

}