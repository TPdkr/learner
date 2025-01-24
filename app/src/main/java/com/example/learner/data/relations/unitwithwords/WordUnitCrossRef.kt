package com.example.learner.data.relations.unitwithwords

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["wid", "uid"], indices = [Index("wid"), Index("uid")])
data class WordUnitCrossRef(
    val wid: Int,
    val uid: Int
)