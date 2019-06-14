package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
    val uuid:String,
    val username:String,
    val password:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}