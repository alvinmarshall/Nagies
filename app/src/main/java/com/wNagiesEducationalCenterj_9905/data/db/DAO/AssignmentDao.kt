package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.common.IMAGE_FORMAT
import com.wNagiesEducationalCenterj_9905.common.PDF_FORMAT
import com.wNagiesEducationalCenterj_9905.data.db.Entities.AssignmentEntity

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAssignment(assignmentEntityList: List<AssignmentEntity>)

    @Query("SELECT * FROM assignment WHERE  token = :token AND format = :format")
    fun getStudentAssignmentPDF(token: String, format: String = PDF_FORMAT): LiveData<List<AssignmentEntity>>

    @Query("DELETE FROM assignment WHERE token = :token AND  format = :format")
    fun deleteAssignmentPDF(token: String,format: String = PDF_FORMAT)

    @Query("SELECT * FROM assignment WHERE token = :token AND format = :format")
    fun getAssignmentImage(token: String, format: String = IMAGE_FORMAT): LiveData<List<AssignmentEntity>>

    @Query("DELETE FROM assignment WHERE  token = :token AND format = :format")
    fun deleteAssignmentImage(token: String,format: String = IMAGE_FORMAT)

    @Query("UPDATE assignment SET path = :path WHERE id = :id")
    fun updateAssignmentPath(path: String, id: Int): Int

    @Query("DELETE FROM assignment WHERE id = :id")
    fun deleteAssignmentById(id: Int)
}