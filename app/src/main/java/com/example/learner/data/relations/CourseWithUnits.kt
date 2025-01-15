package com.example.learner.data.relations

/**this data class stores the info about the relation between units and courses. It is 1:n relation
 * as one course can relate to 0 or many units*/
/*data class CourseWithUnits (
    @Embedded val courseEntity: CourseEntity,
    @Relation(
        parentColumn = "cid",//in courses table
        entityColumn = "courseId",//in units table.
        entity = UnitEntity::class
    )
    val units: List<CourseEntity>
)
//methods to fetch this are in the course package in ..*/