package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class UserEntity(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var uid: String? = null
    var role: String? = null
    var token: String? = null
    var photo: String? = null
    var name:String?=null
    var level:String?=null
}