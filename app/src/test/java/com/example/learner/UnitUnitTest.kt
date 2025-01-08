package com.example.learner

import com.example.learner.classes.CourseUnit
import com.example.learner.classes.Word
import com.example.learner.data.testWords
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

/**Test the basic unit functions and their functionality*/
class UnitUnitTest {
    private val testUnitWords = testWords
    private val testUnit = CourseUnit(testUnitWords, "test", 1, "")
    private val emptyUnit = CourseUnit(listOf(), "", 0, "")

    @Test
    fun canLearnTest() {
        assertTrue(testUnit.canLearn())
    }

    @Test
    fun wordsToLearnTest() {
        //we try a normal looking unit
        assertEquals(testUnitWords, testUnit.wordsToLearn())
        assertEquals(listOf<Word>(), testUnit.wordsToReview())
        assertEquals(listOf<Word>(), testUnit.wordsLearned())
        //we try the corner case of an empty unit
        assertEquals(listOf<Word>(), emptyUnit.wordsToLearn())
        assertEquals(listOf<Word>(), emptyUnit.wordsToReview())
        assertEquals(listOf<Word>(), emptyUnit.wordsLearned())
    }

    @Test
    fun getProgressTest() {
        assertEquals(0F, testUnit.getProgress())
    }
}