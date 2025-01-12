package com.example.learner.data.unit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "units")
class UnitEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val desc: String,
    val number: Int,
)