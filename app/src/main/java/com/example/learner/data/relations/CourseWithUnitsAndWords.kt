package com.example.learner.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.learner.classes.Course
import com.example.learner.data.course.CourseEntity
import com.example.learner.data.relations.unitwithwords.UnitWithWords
import com.example.learner.data.unit.UnitEntity
import com.example.learner.ui.viewModels.UnitCatUiState

data class CourseWithUnitsAndWords(
    @Embedded val courseEntity: CourseEntity,
    @Relation(
        parentColumn = "cid",
        entityColumn = "courseId",
        entity = UnitEntity::class
    )
    val unitsWithWords: List<UnitWithWords>
) {
    fun toCourse(): Course {
        return Course(
            units = unitsWithWords.map { it.toCourseUnit() },
            name = courseEntity.name,
            cid = courseEntity.cid
        )
    }
    fun toCourseUnitUi(): UnitCatUiState{
        val course = toCourse()
        return UnitCatUiState(units = course.units, courseName = course.name)
    }
}