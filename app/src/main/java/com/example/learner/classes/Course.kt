package com.example.learner.classes

data class Course(val units: List<CourseUnit>, val name: String){
    private fun wordsToLearn(): List<Word>{
        val words = mutableListOf<Word>()
        units.forEach { words+=(it.wordsToLearn()) }
        return words
    }

    private fun wordsToReview(): List<Word>{
        val words = mutableListOf<Word>()
        units.forEach { words+=(it.wordsToReview()) }
        return words
    }

    fun learnLesson(): Lesson = Lesson.fromWords(wordsToLearn().take(5))
    fun reviewLesson(): Lesson = Lesson.fromWords(wordsToReview().take(10))
}