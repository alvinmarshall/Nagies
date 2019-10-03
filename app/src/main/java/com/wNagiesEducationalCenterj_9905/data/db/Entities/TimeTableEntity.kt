package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wNagiesEducationalCenterj_9905.vo.IFileModel

@Entity(tableName = "timetable")
data class TimeTableEntity(
    override val studentName: String,
    override val teacherEmail: String,
    override var fileUrl: String?,
    override val date: String,
    override var format: String?,
    override var token: String?,
    override var path: String?,
    override val studentNo: String
) : IFileModel {
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0
}