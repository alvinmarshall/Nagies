package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.vo.IFileModel

@Entity(tableName = "billing")
data class BillingEntity(
    override val studentName: String,
    @SerializedName("sender")
    override val teacherEmail: String,
    override var fileUrl: String?,
    override val date: String,
    override var format: String?,
    override var token: String?,
    override var path: String?

) :IFileModel{
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0
}