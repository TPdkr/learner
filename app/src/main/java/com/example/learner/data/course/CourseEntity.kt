package com.example.learner.data.course

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
class CourseEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)