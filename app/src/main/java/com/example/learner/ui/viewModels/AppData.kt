package com.example.learner.ui.viewModels

import com.example.learner.classes.CourseUnit
import com.example.learner.classes.Lesson

object AppData {
    var lesson: Lesson = Lesson(listOf())
    var unit: CourseUnit = CourseUnit(listOf(), "", 0, "")//delete line
    var unitUid: Int = -1
    var CourseId: Int = -1
}