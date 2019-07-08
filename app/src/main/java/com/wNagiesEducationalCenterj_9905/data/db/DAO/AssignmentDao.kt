package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.AssignmentEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAssignmentPDF(assignmentPDFEntityList: List<AssignmentEntity>)

    @Query("SELECT * FROM assignment WHERE  token = :token AND type = :type")
    fun getStudentAssignmentPDF(token: String, type: String = "pdf"): LiveData<List<AssignmentEntity>>

    @Query("DELETE FROM assignment WHERE type = :type")
    fun deleteAssignmentPDF(type: String = "pdf")

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAssignmentImage(assignmentImageEntityList: List<AssignmentEntity>)

    @Query("SELECT * FROM assignment WHERE token = :token AND type = :type")
    fun getAssignmentImage(token: String, type: String = "image"): LiveData<List<AssignmentEntity>>

    @Query("DELETE FROM assignment WHERE type = :type")
    fun deleteAssignmentImage(type: String = "image")

    @Query("UPDATE assignment SET path = :path WHERE id = :id")
    fun updateAssignmentPath(path:String,id:Int):Int

    @Query("DELETE FROM assignment WHERE id = :id")
    fun deleteAssignmentById(id: Int)
}