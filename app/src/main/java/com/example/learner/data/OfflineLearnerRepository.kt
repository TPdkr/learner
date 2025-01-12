package com.example.learner.data

import com.example.learner.data.word.WordDao
import com.example.learner.data.word.WordEntity
import kotlinx.coroutines.flow.Flow

class OfflineLearnerRepository(private val wordDao: WordDao): LearnerRepository {
    override fun getWordStream(id: Int): Flow<WordEntity?> = wordDao.getWord(id)

    override suspend fun updateWord(word: WordEntity) = wordDao.update(word)

    override suspend fun deleteWord(word: WordEntity) = wordDao.delete(word)

    override suspend fun insertWord(word: WordEntity) = wordDao.insert(word)
}