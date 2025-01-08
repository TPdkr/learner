package com.example.learner

import com.example.learner.classes.Gender
import com.example.learner.classes.Plural
import com.example.learner.classes.Status
import com.example.learner.classes.Word
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

/**Test the basic functionality of the word data class*/
class WordUnitTest {
    private val testNoun = Word("Auto", "Car", gender = Gender.DAS, plural = Plural.S)
    private val testWord2 = Word("laut", "loud")

    @Test
    fun isNounTest() {
        assertEquals(true, testNoun.isNoun())
        assertEquals(false, testWord2.isNoun())
    }

    @Test
    fun getStatusTest() {
        assertEquals(testNoun.getWordStatus(), Status.NEW)
    }

    @Test
    fun uiStringTest() {
        assertEquals("Das Auto -s = Car", testNoun.toUiString())
        assertEquals(" laut  = loud", testWord2.toUiString())
    }

    @Test
    fun resetAndIncTest() {
        testNoun.incMistakes()
        testNoun.incRound()
        assertEquals(testNoun.mistakes, 1)
        assertEquals(testNoun.round, 1)
        testNoun.resetLesson()
        assertEquals(testNoun.mistakes, 0)
        assertEquals(testNoun.round, 0)
    }

    @Test
    fun scoreNounTest() {
        //normal fully correct answer
        var score = testNoun.countScore(2, "Auto", 3)
        assertEquals(20, score)
        //partially correct answers:
        score = testNoun.countScore(1, "Auto", 3)
        assertEquals(15, score)
        score = testNoun.countScore(2, "Aujhaskfhdskj", 3)
        assertEquals(10, score)
        score = testNoun.countScore(2, "Auto", 1)
        assertEquals(15, score)
        //check for spaces at the end affecting score
        score = testNoun.countScore(2, "Auto   ", 3)
        assertEquals(20, score)
        //check that completely wrong gives 0
        score = testNoun.countScore(1, "", 2)
        assertEquals(0, score)
    }

    @Test
    fun scoreTest2() {
        var score = testWord2.countScore(-1, "laut", -1)
        assertEquals(20, score)
        score = testWord2.countScore(1, "laut  ", 2)
        assertEquals(20, score)
        score = testWord2.countScore(1, "loud", 2)
        assertEquals(0, score)

    }

    @Test
    fun isCorrectNounTest() {
        //normal fully correct answer
        var isCorrect = testNoun.isCorrect(2, "Auto", 3)
        assertEquals(true, isCorrect)
        //partially correct answers:
        isCorrect = testNoun.isCorrect(1, "Auto", 3)
        assertEquals(false, isCorrect)
        isCorrect = testNoun.isCorrect(2, "Aujhaskfhdskj", 3)
        assertEquals(false, isCorrect)
        isCorrect = testNoun.isCorrect(2, "Auto", 1)
        assertEquals(false, isCorrect)
        //check for spaces at the end affecting score
        isCorrect = testNoun.isCorrect(2, "Auto   ", 3)
        assertEquals(true, isCorrect)
        //check that completely wrong gives 0
        isCorrect = testNoun.isCorrect(1, "", 2)
        assertEquals(false, isCorrect)
    }

    @Test
    fun isCorrectTest2() {
        var isCorrect = testWord2.isCorrect(-1, "laut", -1)
        assertEquals(true, isCorrect)
        isCorrect = testWord2.isCorrect(1, "laut  ", 2)
        assertEquals(true, isCorrect)
        isCorrect = testWord2.isCorrect(1, "loud", 2)
        assertEquals(false, isCorrect)
    }
    //maybe more word status and save progress
}