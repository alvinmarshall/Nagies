package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class UserEntity (
    val userId:String?,
    @SerializedName("username")
    val username:String,
    @SerializedName("password")
    val password:String,
    val token:String,
    val photo:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
    var role:String = ""
}