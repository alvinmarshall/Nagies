package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class StudentProfileEntity(
    val studentNo: String,
    val studentName: String,
    val dob: String,
    val gender: String,
    val admissionDate: String,
    val section: String,
    val faculty: String,
    val level: String,
    val semester: String,
    val index: String,
    val guardian: String,
    val contact: String,
    var imageUrl: String?,
    var token:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}