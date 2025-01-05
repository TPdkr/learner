package com.example.learner.classes

/**Course Unit class stores info about a unit in a course. It is defined by [name],[desc]-
 * description and a number [number]. The contents are in [words] list*/
data class CourseUnit(val words: List<Word>, val name: String, val number: Int, val desc: String) {
    /**get all ready to learn words*/
    fun wordsToLearn(): List<Word> =
        words.filter { it.getWordStatus() == Status.NEW || it.getWordStatus() == Status.LEARNING }

    /**get words ready to review*/
    fun wordsToReview(): List<Word> =
        words.filter { it.getWordStatus() == Status.REVIEW }

    /**get words that are in long term or permanent memory*/
    fun wordsLearned(): List<Word> =
        words.filter {it.getWordStatus()==Status.LONG_TERM || it.getWordStatus()==Status.MEMORIZED}
    /**value between 0 and 1 that shows how complete is the Unit*/
    fun getProgress(): Float = if (words.isNotEmpty()) wordsLearned().size.toFloat()/words.size.toFloat() else 0F

    /**get a lesson to learn unit*/
    fun learnLesson(): Lesson =
        Lesson.fromWords(wordsToLearn().take(5))
}