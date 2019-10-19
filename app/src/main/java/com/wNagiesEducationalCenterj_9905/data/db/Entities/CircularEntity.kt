package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "circular")
data class CircularEntity(
    val cid: String,
    var fileUrl: String?,
    val date: String,
    var token:String?,
    var path:String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}