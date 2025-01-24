package com.example.learner.data.word


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.learner.classes.Gender
import com.example.learner.classes.Plural
import com.example.learner.classes.Word
import java.util.Calendar

/**The table that stores both the word and the users progress on it*/
@Entity(tableName = "words")
class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val wid: Int,
    val german: String,//maybe enforce unique somehow
    val translation: String,
    val gender: Int = -1,
    val plural: Int = -1,
    val revision: Int = 0,
    val revisionTime: Long = 0
) {
    fun toWord(): Word {
        return Word(
            wid=wid,
            german = german,
            translation = translation,
            gender = Gender.fromCode(gender),
            plural = Plural.fromCode(plural),
            revision = revision,
            revisionTime = Calendar.getInstance().apply {
                this.timeInMillis=revisionTime
            } ?: throw IllegalStateException("Unable to get an instance of Calendar")
        )
    }
}