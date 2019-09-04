package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class_student")
data class ClassStudentEntity(
    val studentNo: String,
    val studentName: String,
    val gender: String,
    val indexNo: String,
    var imageUrl: String?,
    var token: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}