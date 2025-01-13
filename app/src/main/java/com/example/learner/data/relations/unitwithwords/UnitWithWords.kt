package com.example.learner.data.relations.unitwithwords

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
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
)