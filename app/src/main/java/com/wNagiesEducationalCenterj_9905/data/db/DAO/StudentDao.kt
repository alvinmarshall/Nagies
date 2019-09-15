package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.*

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentProfile(studentProfileEntity: StudentProfileEntity)

    @Query("SELECT * FROM profile WHERE token = :token")
    fun getStudentProfile(token: String): LiveData<StudentProfileEntity>

    @Query("DELETE FROM profile WHERE token = :token")
    fun deleteProfile(token: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentTeacher(studentTeacherEntityList: List<StudentTeacherEntity>)

    @Query("SELECT * FROM student_teacher WHERE token = :token AND teacherName LIKE :search")
    fun getClassTeacher(token: String, search: String): LiveData<List<StudentTeacherEntity>>

    @Query("DELETE FROM student_teacher WHERE token = :token")
    fun deleteClassTeacher(token: String)


    //region Circular

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCircular(circularEntityList: List<CircularEntity>)

    @Query("SELECT * FROM circular WHERE token = :token")
    fun getCircular(token: String): LiveData<List<CircularEntity>>

    @Query("DELETE FROM circular WHERE token = :token")
    fun deleteCircular(token: String)

    @Query("UPDATE circular SET filePath = :file WHERE id = :id")
    fun updateCircularImagePath(id: Int, file: String): Int
    //endregion

    //region Billing

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentBills(billingEntityList: List<BillingEntity>)

    @Query("SELECT * FROM billing WHERE token = :token")
    fun getStudentBills(token: String): LiveData<List<BillingEntity>>

    @Query("DELETE FROM billing WHERE token = :token")
    fun deleteStudentBill(token: String)

    @Query("UPDATE billing SET path = :file WHERE id = :id")
    fun updateBillingImagePath(id: Int, file: String): Int

    @Query("DELETE FROM billing WHERE id = :id")
    fun deleteBillingById(id: Int)
    //endregion

    //region timetable
    @Insert
    fun insertTimetable(timeTableEntity: List<TimeTableEntity>)

    @Query("SELECT * FROM timetable WHERE token = :token")
    fun getStudentTimetable(token: String): LiveData<List<TimeTableEntity>>

    @Query("DELETE FROM timetable WHERE token = :token")
    fun deleteTimetableById(token: String)

    @Query("UPDATE timetable SET path = :file WHERE id = :id")
    fun updateTimetableImagePath(id: Int, file: String): Int

    //endregion
}