package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.common.IMAGE_FORMAT
import com.wNagiesEducationalCenterj_9905.common.PDF_FORMAT
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ReportEntity

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReport(reportEntityList: List<ReportEntity>)

    @Query("SELECT * FROM report WHERE token = :token AND format = :format")
    fun getStudentReportPDF(token: String, format: String = PDF_FORMAT): LiveData<List<ReportEntity>>

    @Query("SELECT * FROM report WHERE token = :token AND format = :format")
    fun getStudentReportImage(token: String, format: String = IMAGE_FORMAT): LiveData<List<ReportEntity>>

    @Query("DELETE FROM report where token = :token AND  format = :format")
    fun deleteReportPDF(token: String,format: String = PDF_FORMAT)

    @Query("DELETE FROM report where token = :token AND  format = :format")
    fun deleteReportImage(token: String,format: String = IMAGE_FORMAT)

    @Query("UPDATE report SET path = :path WHERE id = :id")
    fun updateReportPath(path: String, id: Int): Int

    @Query("DELETE FROM report WHERE id = :id")
    fun deleteReportById(id: Int)

}
