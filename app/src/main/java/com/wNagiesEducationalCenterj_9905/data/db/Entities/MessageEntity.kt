package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    val sender: String,
    val level: String,
    val content: String,
    val read: String,
    val token:String
    ){
    @PrimaryKey(autoGenerate = true)
    var id:Int= 0
}