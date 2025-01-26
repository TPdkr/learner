package com.example.learner.data.word

import kotlinx.coroutines.flow.Flow

interface WordRepository {
    /**get a word by id*/
    fun getWordStream(id: Int): Flow<WordEntity?>

    /**get all words in the database*/
    fun getAllWords(): Flow<List<WordEntity>>

    /**get the number of completed words*/
    fun getDoneWordCount(): Flow<Int>

    /**insert a word into the database*/
    suspend fun insertWord(word: WordEntity): Long

    /**update a word in a database*/
    suspend fun updateWord(word: WordEntity)

    /**delete a word in a database*/
    suspend fun deleteWord(word: WordEntity)

    /**delete all entries*/
    suspend fun clear()

    /**delete a word by its ID*/
    suspend fun deleteById(id: Int)
}