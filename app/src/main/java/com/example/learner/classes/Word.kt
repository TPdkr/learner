package com.example.learner.classes

import androidx.compose.ui.util.fastJoinToString
import com.example.learner.data.word.WordEntity
import com.example.learner.data.word.WordRepository
import java.util.Calendar
import kotlin.math.max
import kotlin.math.pow

/**
WORD STATUS:
A word can be either new, learning or in long term memory. The final stage is memorized.
 */
enum class Status {
    NEW,
    LEARNING,
    LONG_TERM,
    MEMORIZED,
    REVIEW
}

/**
NOUN GENDER:
This is a enum class used for storing a gender of a noun
 */
enum class Gender(val code: Int) {
    DER(0),
    DIE(1),
    DAS(2),
    NOT_SET(-1);

    companion object {
        fun fromCode(code: Int): Gender {
            return when (code) {
                0 -> DER
                1 -> DIE
                2 -> DAS
                else -> NOT_SET
            }
        }
    }
}

/**
NOUN PLURAL FORM:
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
    NOT_SET(-1);

    companion object {
        fun fromCode(code: Int): Plural {
            return when (code) {
                0 -> NO_CHANGE
                1 -> E
                2 -> E_UMLAUT
                3 -> S
                4 -> ER_UMLAUT
                5 -> EN
                6 -> N
                else -> NOT_SET
            }
        }
    }
}

/**
WORD DATA CLASS:
This stores the information of a word that can be a noun and not a noun. I decided on that as
inheritance with data classes in Kotlin is a bit fucked apparently.
 */
data class Word(
    val wid: Int = 0,
    //common word properties:
    val german: String,
    val translation: String,
    //properties relevant for lesson contents:
    /**number of mistakes made during 1 lesson*/
    var mistakes: Int = 0,
    /**the time a word comes up in a given lesson*/
    var round: Int = 0,
    /**the time a word is revised, how many lessons it has been in?*/
    var revision: Int = 0,
    //noun properties:
    val gender: Gender = Gender.NOT_SET,
    val plural: Plural = Plural.NOT_SET,
    var revisionTime: Calendar = Calendar.getInstance()
) {
    /**calculate the status of the word using its data. Mainly [revisionTime] and [revision]*/
    fun getWordStatus(): Status {
        val currentTime = Calendar.getInstance().timeInMillis
        return when {
            (revision == 0) -> Status.NEW
            (revision == -1) -> Status.MEMORIZED
            (revision == 1) -> Status.LEARNING
            (revisionTime.timeInMillis <= currentTime) -> Status.REVIEW
            else -> Status.LONG_TERM
        }
    }

    /**when should a word be revised?*/
    //var revisionTime: Calendar //can this cause issues?!!!

    /*init {
        revisionTime = Calendar.getInstance()
    }*/

    /**turn key info of a class instance into a readable string containing the [gender], [german]
     * translation and [plural] form as well as the [translation] into users language*/
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

    /**reset [mistakes] and [round] to 0 while increasing the [revision] count. Should be called
     * at the end of the lesson*/
    fun resetLesson() {
        mistakes = 0
        round = 0
    }

    /**is an instance a noun or not?*/
    fun isNoun(): Boolean = gender != Gender.NOT_SET && plural != Plural.NOT_SET

    /**is a guess correct?*/
    fun isCorrect(genderGuess: Int, germanGuess: String, pluralGuess: Int): Boolean =
        germanGuess.trimEnd().equals(german, ignoreCase = true)
                && if (isNoun()) (genderGuess == (gender.code) && (pluralGuess == (plural.code))) else true

    /**turn a word into a word entity*/
    private fun toWordEntity(): WordEntity {
        return WordEntity(
            wid,
            german,
            translation,
            gender.code,
            plural.code,
            revision,
            revisionTime.timeInMillis
        )
    }

    /**save progress and revise the word later*/
    suspend fun saveProgress(wordRepository: WordRepository) {
        if (revision != -1) {
            revision++
            val newRevisionTime = Calendar.getInstance()//we get current time
            //how many hours to add is calculated
            var hours =
                12.0 + 24.0 * (revision.toDouble() - 1.0).pow(2.0) -
                        60.0 * (mistakes.toDouble()).pow(2.0)
            if(hours<0){
                hours = 5.0
            }
            //i turn it into minutes and randomize a bit in order to not review everything at once
            val minutes = (hours * 60.0 * ((7..13).random().toDouble() / 10.0)).toInt()
            //we update revision time
            newRevisionTime.add(Calendar.HOUR_OF_DAY, minutes / 60)
            newRevisionTime.add(Calendar.MINUTE, minutes % 60)
            //we change the word state to memorized
            revisionTime = newRevisionTime
            if (hours / 24 > 100) {
                revision = -1
            }
            //we save the state of the word
            wordRepository.updateWord(this.toWordEntity())
        }
    }

    /**get revision time of the word*/
    fun getRevisionTime(): String{
        var dif = revisionTime.timeInMillis-Calendar.getInstance().timeInMillis
        dif /=1000
        dif/=60
        return when{
            (revision==-1)->"learned"
            revision<2->"-"
            (dif)<0->"revision"
            (dif<60)->"in $dif min"
            (dif/60<24)->"in ${dif/60} h"
            else -> "in ${dif/60/24} d"
        }
    }

    /**increase round count of a word*/
    fun incRound() {
        round++
    }

    /**increase mistake count of a word*/
    fun incMistakes() {
        mistakes++
    }

    /**count a score a given guess should receive varies from 20 to 0 based on context*/
    fun countScore(genderGuess: Int, germanGuess: String, pluralGuess: Int): Int {
        //we calculate key values:
        val isCorrect = isCorrect(genderGuess, germanGuess, pluralGuess)
        val isGenderCorrect = if (genderGuess == gender.code) 0F else 1F
        val isGermanCorrect =
            if (germanGuess.trimEnd().equals(german, ignoreCase = true)) 0F else 1F
        val isPluralCorrect = if (pluralGuess == plural.code) 0F else 1F
        //the value between 0 and 1 telling us how close the guess was
        val guessScore = if (isNoun()) {
            isPluralCorrect * 0.25F + isGenderCorrect * 0.25F + isGermanCorrect * 0.5F
        } else {
            isGermanCorrect
        }
        //we want to account for the attempt count, the higher the more points are taken off
        val attemptMargin = if (!isCorrect) round.toFloat() * 0.5F else 0F
        //return the final value
        return max((20 - guessScore * 20 - attemptMargin).toInt(), 0)
    }
}