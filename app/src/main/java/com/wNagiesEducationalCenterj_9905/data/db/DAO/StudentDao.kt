package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.StudentProfileEntity

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudentProfile(studentProfileEntity: StudentProfileEntity)

    @Query("SELECT * FROM profile WHERE token = :token")
    fun getStudentProfile(token: String): LiveData<StudentProfileEntity>
}