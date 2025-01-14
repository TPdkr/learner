package com.example.learner.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userdata")
class UserEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val currentCourseId: Int,
    val xp: Int
)