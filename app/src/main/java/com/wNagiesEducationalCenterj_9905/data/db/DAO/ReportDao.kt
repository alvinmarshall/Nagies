package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ReportEntity

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReport(reportEntityList: List<ReportEntity>)

    @Query("SELECT * FROM report WHERE token = :token AND format = :format ORDER BY id DESC")
    fun getStudentReportPDF(token: String, format: String = "pdf"): LiveData<List<ReportEntity>>

    @Query("SELECT * FROM report WHERE token = :token AND format = :format ORDER BY id DESC")
    fun getStudentReportImage(token: String, format: String = "image"): LiveData<List<ReportEntity>>

    @Query("DELETE FROM report where format = :format")
    fun deleteReportPDF(format: String = "pdf")

    @Query("DELETE FROM report where format = :format")
    fun deleteReportImage(format: String = "image")

    @Query("UPDATE report SET path = :path WHERE id = :id")
    fun updateReportPath(path: String, id: Int): Int

    @Query("DELETE FROM report WHERE id = :id")
    fun deleteReportById(id: Int)

}