package com.example.learner.classes

data class CourseUnit(val words: List<Word>, val name: String, val number: Int, val desc: String) {
    /**get all ready to learn words*/
    fun wordsToLearn(): List<Word> =
        words.filter { it.status == Status.NEW || it.status == Status.LEARNING }
    /**get words ready to review*/
    fun wordsToReview(): List<Word> =
        words.filter {it.status == Status.REVIEW}
    /**get a lesson to learn unit*/
    fun learnLesson(): Lesson =
        Lesson.fromWords(wordsToLearn().take(5))
    fun reviewLesson(): Lesson =
        Lesson.fromWords(wordsToReview().take(10))
}