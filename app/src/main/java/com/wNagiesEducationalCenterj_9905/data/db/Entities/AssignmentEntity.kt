package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignment")
data class AssignmentEntity(
    val studentName: String,
    val teacherEmail: String,
    var fileUrl: String?,
    val date: String,
    var format: String?,
    var token: String?,
    var path: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}