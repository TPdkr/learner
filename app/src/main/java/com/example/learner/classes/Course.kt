package com.example.learner.classes

/**A course instance stores a list of [units] and has a [name], which is displayed in catalogue and
 *  on units page*/
data class Course(val units: List<CourseUnit>, val name: String) {
    /**get all ready to learn words*/
    private fun wordsToLearn(): List<Word> {
        val words = mutableListOf<Word>()
        units.forEach { words += (it.wordsToLearn()) }
        return words.toList()
    }

    /**get words ready to review*/
    private fun wordsToReview(): List<Word> {
        val words = mutableListOf<Word>()
        units.forEach { words += (it.wordsToReview()) }
        return words.toList()
    }

    /**get words that are in long term or permanent memory*/
    private fun wordsLearned(): List<Word> {
        val words = mutableListOf<Word>()
        units.forEach { words += (it.wordsLearned()) }
        return words.toList()
    }

    /**get all words in a course*/
    private fun wordsAll(): List<Word> {
        val words = mutableListOf<Word>()
        units.forEach { words += (it.words) }
        return words.toList()
    }

    /**value between 0 and 1 that shows how complete is the Course*/
    fun getProgress(): Float =
        if (wordsAll().isNotEmpty()) wordsLearned().size.toFloat() / wordsAll().size.toFloat() else 0F

    /**get a lesson to learn new words*/
    fun learnLesson(): Lesson = Lesson.fromWords(wordsToLearn().take(5))

    /**get a lesson to review learned words*/
    fun reviewLesson(): Lesson = Lesson.fromWords(wordsToReview().take(10), isReview = true)

    /**can i review this course? return a boolean*/
    fun canReview(): Boolean = wordsToReview().isNotEmpty()

    /**can i learn in this course? returns a boolean */
    fun canLearn(): Boolean = wordsToLearn().isNotEmpty()

    /**how many words to review at the moment?*/
    fun reviewCount(): Int = wordsToReview().size
}