package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parent_complaint")
data class ComplaintEntity(
    val content: String,
    val date: String,
    var token:String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}