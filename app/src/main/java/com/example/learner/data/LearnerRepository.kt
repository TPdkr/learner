package com.example.learner.data

import com.example.learner.data.word.WordEntity
import kotlinx.coroutines.flow.Flow

interface LearnerRepository {
    /**get a word by id*/
    fun getWordStream(id: Int): Flow<WordEntity?>

    /**insert a word into the database*/
    suspend fun insertWord(word: WordEntity)

    /**update a word in a database*/
    suspend fun updateWord(word: WordEntity)

    /**delete a word in a database*/
    suspend fun deleteWord(word: WordEntity)
}