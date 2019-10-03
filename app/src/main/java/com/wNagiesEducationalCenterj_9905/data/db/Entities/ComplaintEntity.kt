package com.wNagiesEducationalCenterj_9905.data.db.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wNagiesEducationalCenterj_9905.vo.IComplaintModel

@Entity(tableName = "complaint")
data class ComplaintEntity(

    override val studentNo: String,
    override val studentName: String,
    override val level: String,
    override val guardianName: String,
    override val guardianContact: String,
    override val teacherName: String,
    override val message: String,
    override val date: String,
    override var token: String?

) : IComplaintModel {
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0
    override var uid: Int = 0
}