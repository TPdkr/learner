package com.example.learner.classes

import android.util.Log

/**
LESSON DATA CLASS
This is where information about a lesson is stored
 */
enum class TaskType {
    TYPE_TEXT,
    INFO
}

/** Lesson data class stores lesson information. It has a secondary constructor that allows to
 * create a lesson from a list of words
 * */
data class Lesson(
    val tasks: List<Pair<Word, TaskType>>
) {
    //this function repeats a list of words a given number of times
    companion object {
        //constants
        private const val newWordTaskCount = 2
        private const val learningWordTaskCount = 3
        private const val reviewWordTaskCount = 2

        //this is a substitute for secondary constructor
        fun fromWords(words: List<Word>): Lesson {
            val tasks = generateTasks(words)
            return Lesson(tasks)
        }

        //repeat the contents of a list of words
        private fun repeatList(words: List<Word>, count: Int): List<Word> {
            val result = mutableListOf<Word>()
            for (word in words) {
                repeat(count) {
                    result.add(word)
                }
            }
            return result.toList()
        }

        //this function generates the tasks based on context
        private fun generateTasks(words: List<Word>): List<Pair<Word, TaskType>> {
            //what type of lesson is it?
            val isReview = words.all { it.status == Status.REVIEW }
            val isLearn = words.all { it.status == Status.NEW || it.status == Status.LEARNING }
            //we take actions based on type:
            if (!isLearn && !isReview || isLearn && isReview) {
                Log.e("Lesson constructor", "Invalid word set")
                return listOf()
            } else if (isLearn) {
                //the list of all new words and learning words
                val newWords = words.filter { it.status == Status.NEW }
                val learnWords = words.filter { it.status == Status.LEARNING }
                //tasks for each type
                val newWordsInfo = newWords.map { Pair(it, TaskType.INFO) }// info cards
                //practice part
                val newWordsPract =
                    repeatList(newWords, newWordTaskCount).map { Pair(it, TaskType.TYPE_TEXT) }
                val learningWords =
                    repeatList(learnWords, learningWordTaskCount).map {
                        Pair(it, TaskType.TYPE_TEXT)
                    }
                //we combine the practice tasks
                val practice = (newWordsPract + learningWords).shuffled()
                val totalTasks = newWordsInfo + practice
                //finally return the value
                return totalTasks
            } else {
                val totalTasks =
                    repeatList(words, reviewWordTaskCount).map { Pair(it, TaskType.TYPE_TEXT) }
                return totalTasks
            }
        }
    }
}