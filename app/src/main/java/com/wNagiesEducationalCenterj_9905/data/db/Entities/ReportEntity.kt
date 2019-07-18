package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report")
data class ReportEntity(
    val studentName: String,
    val teacherEmail: String,
    var fileUrl: String?,
    var format: String?,
    val date: String,
    var token:String?,
    var path:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}