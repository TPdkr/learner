package com.example.learner.data.word

import androidx.room.Entity
import androidx.room.PrimaryKey

/**The table that stores both the word and the users progress on it*/
@Entity(tableName = "words")
class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val wid: Int,
    val german: String,//maybe enforce unique somehow
    val gender: Int = -1,
    val plural: Int = -1,
    val revision: Int = 0,
    val revisionTime: Long=0
)