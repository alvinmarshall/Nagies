package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ClassStudentEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherProfileEntity

@Dao
interface TeacherDao {
    @Insert
    fun insertTeacherProfile(teacherProfileEntity: TeacherProfileEntity)

    @Query("SELECT * FROM teacher_profile WHERE token = :token")
    fun getTeacherProfile(token: String): LiveData<TeacherProfileEntity>

    @Query("DELETE FROM teacher_profile WHERE token = :token")
    fun deleteTeacherProfile(token: String)

    @Insert
    fun insertClassStudent(classStudentEntity: List<ClassStudentEntity>)

    @Query("SELECT * FROM class_student WHERE token = :token")
    fun getClassStudent(token: String): LiveData<List<ClassStudentEntity>>

    @Query("SELECT * FROM class_student WHERE token = :token AND studentName LIKE :search")
    fun searchClassStudent(token: String,search:String): LiveData<List<ClassStudentEntity>>

    @Query("DELETE FROM class_student WHERE token = :token")
    fun deleteClassStudent(token: String)

}