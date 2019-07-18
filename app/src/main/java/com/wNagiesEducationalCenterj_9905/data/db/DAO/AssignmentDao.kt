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

    @Query("SELECT * FROM assignment WHERE  token = :token AND format = :format")
    fun getStudentAssignmentPDF(token: String, format: String = "pdf"): LiveData<List<AssignmentEntity>>

    @Query("DELETE FROM assignment WHERE format = :format")
    fun deleteAssignmentPDF(format: String = "pdf")

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAssignmentImage(assignmentImageEntityList: List<AssignmentEntity>)

    @Query("SELECT * FROM assignment WHERE token = :token AND format = :format")
    fun getAssignmentImage(token: String, format: String = "image"): LiveData<List<AssignmentEntity>>

    @Query("DELETE FROM assignment WHERE format = :format")
    fun deleteAssignmentImage(format: String = "image")

    @Query("UPDATE assignment SET path = :path WHERE id = :id")
    fun updateAssignmentPath(path: String, id: Int): Int

    @Query("DELETE FROM assignment WHERE id = :id")
    fun deleteAssignmentById(id: Int)
}