package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherProfileEntity

@Dao
interface TeacherDao {
    @Insert
    fun insertTecherProfile(teacherProfileEntity: TeacherProfileEntity)

    @Query("SELECT * FROM teacher_profile WHERE token = :token")
    fun getTeacherProfile(token: String): LiveData<TeacherProfileEntity>

    @Query("DELETE FROM teacher_profile WHERE token = :token")
    fun deleteTeacherProfile(token: String)

}