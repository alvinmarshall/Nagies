package com.cheise_proj.local_source.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocal(
    @PrimaryKey(autoGenerate = true)
    var uuid: Int,
    val username: String,
    var password: String,
    val role: String,
    var photo: String?,
    val name: String,
    val level: String,
    val token: String
)