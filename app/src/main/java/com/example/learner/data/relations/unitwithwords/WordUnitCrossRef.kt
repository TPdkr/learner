package com.example.learner.data.relations.unitwithwords

import androidx.room.Entity

@Entity(primaryKeys = ["wid", "uid"])
data class WordUnitCrossRef(
    val wid: Int,
    val uid: Int
)