package com.example.learner.classes

import android.util.Log

enum class TaskType {
    TYPE_TEXT,
    INFO
}

/** Lesson data class stores lesson information. It has a function [fromWords] that allows to
 * create a lesson from a list of words
 * */
data class Lesson(
    val tasks: List<Pair<Word, TaskType>>
) {
    //this function repeats a list of words a given number of times
    companion object {
        //constants
        private const val NEW_WORD_TASK_COUNT = 2
        private const val LEARNING_WORD_TASK_COUNT = 3
        private const val REVIEW_WORD_TASK_COUNT = 2

        /**this is a substitute for secondary constructor as not to confuse the compiler and cause
         * an error*/
        fun fromWords(words: List<Word>, isReview: Boolean = false): Lesson {
            val tasks = generateTasks(words, isReview)
            Log.d("lesson bug", tasks.joinToString(" ") { it.first.german })
            Log.d("lesson bug", tasks.joinToString(" ") { it.second.name })
            return Lesson(tasks)
        }

        /**repeat the contents of a list of words*/
        private fun repeatList(words: List<Word>, count: Int): List<Word> {
            val result = mutableListOf<Word>()
            for (word in words) {
                repeat(count) {
                    result.add(word)
                }
            }
            return result.toList()
        }

        /**this function generates the tasks based on context. Its only input is a list of words*/
        private fun generateTasks(words: List<Word>, isReview: Boolean): List<Pair<Word, TaskType>> {
            //we take actions based on type:
            if (!isReview) {
                //the list of all new words and learning words
                val newWords = words.filter { it.getWordStatus() == Status.NEW }
                val learnWords = words.filter { it.getWordStatus() == Status.LEARNING }
                //tasks for each type
                val newWordsInfo = newWords.map { Pair(it, TaskType.INFO) }// info cards
                //practice part
                val newWordsPract =
                    repeatList(newWords, NEW_WORD_TASK_COUNT).map { Pair(it, TaskType.TYPE_TEXT) }
                val learningWords =
                    repeatList(learnWords, LEARNING_WORD_TASK_COUNT).map {
                        Pair(it, TaskType.TYPE_TEXT)
                    }
                //we combine the practice tasks
                val practice = (newWordsPract + learningWords).shuffled()
                val totalTasks = newWordsInfo + practice
                //finally return the value
                return totalTasks
            } else {
                val totalTasks =
                    repeatList(words, REVIEW_WORD_TASK_COUNT).map { Pair(it, TaskType.TYPE_TEXT) }
                return totalTasks
            }
        }
    }
    /**save the state of words after the lesson, update and reset key values*/
    fun saveLesson(){
        val words = tasks.map{it.first}.toSet()//we get all words in a lesson
        words.forEach {
            //update state of the word
            Log.d("state check", "Before save: $it, status: ${it.getWordStatus()}, reviewTime: ${it.revisionTime}")
            it.saveProgress()
            it.resetLesson()
            Log.d("state check", "After save: $it, status: ${it.getWordStatus()}, reviewTime ${it.revisionTime}")
        }
    }
}