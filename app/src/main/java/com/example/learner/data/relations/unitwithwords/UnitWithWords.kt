package com.example.learner.data.relations.unitwithwords

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.learner.classes.CourseUnit
import com.example.learner.data.unit.UnitEntity
import com.example.learner.data.word.WordEntity

data class UnitWithWords (
    @Embedded val unitEntity: UnitEntity,
    @Relation(
        parentColumn = "uid",
        entityColumn = "wid",
        associateBy = Junction(WordUnitCrossRef::class)
    )
    val wordEntities:List<WordEntity>
) {
    fun toCourseUnit(): CourseUnit{
        return CourseUnit(
            name = unitEntity.name,
            desc = unitEntity.desc,
            uid = unitEntity.uid,
            number = unitEntity.number,
            words = wordEntities.map{it.toWord()}
        )
    }
}