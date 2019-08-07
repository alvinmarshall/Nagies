package com.wNagiesEducationalCenterj_9905.data.db.Entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teacher_complaint")
data class TeacherComplaintEntity(
    val studentNo: String,
    val studentName: String,
    val level: String,
    val guardianName: String,
    val guardianContact: String,
    val teacherName: String,
    val message: String,
    val date: String,
    var token:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}