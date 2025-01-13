package com.example.learner.data.unit

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.learner.data.course.CourseEntity

//this entity is linked to the course it is in. Each unit is only in one course
@Entity(
    tableName = "units",
    foreignKeys = [ForeignKey(
        entity = com.example.learner.data.course.CourseEntity::class,
        parentColumns = ["id"],//the parent is courses table
        childColumns = ["courseId"],//the child is the units
        onDelete = ForeignKey.CASCADE//if a user deletes a course all its units should be deleted
    )]
)
class UnitEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val name: String,
    val desc: String,
    val number: Int,
    val courseId: Int
)