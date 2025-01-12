package com.example.learner.data.word

import kotlinx.coroutines.flow.Flow

class OfflineWordRepository(private val wordDao: WordDao) : WordRepository {
    override fun getWordStream(id: Int): Flow<WordEntity?> = wordDao.getWord(id)

    override suspend fun updateWord(word: WordEntity) {
        if (isValid(word)) {
            wordDao.update(word)
        }

    }

    override suspend fun deleteWord(word: WordEntity) = wordDao.delete(word)

    override suspend fun insertWord(word: WordEntity) {
        if (isValid(word)) {
            wordDao.insert(word)
        }
    }
    /**is a given word satisfying the integrity constrain?*/
    private fun isValid(word: WordEntity): Boolean =
        (word.gender == -1 && word.plural == -1) || (word.gender != -1 && word.plural != -1)
}