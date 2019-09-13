package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ComplaintEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherComplaintEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ComplaintDao {
    @Insert
    fun insertParentComplaint(complaintEntity: ComplaintEntity): Single<Long>

    @Query("SELECT * FROM parent_complaint WHERE token = :token")
    fun getSavedComplaintMessage(token: String): Flowable<List<ComplaintEntity>>

    @Query("SELECT * FROM parent_complaint WHERE id = :id")
    fun getSavedComplaintMessageById(id: Int): Single<ComplaintEntity>

    //region TeacherProfileEntity
    @Insert
    fun insertTeacherComplaint(teacherComplaintEntityList: List<TeacherComplaintEntity>)

    @Query("SELECT * FROM teacher_complaint WHERE token = :token AND message LIKE :search")
    fun getTeacherComplaintMessage(token: String,search:String): LiveData<List<TeacherComplaintEntity>>

    @Query("DELETE FROM teacher_complaint WHERE token = :token")
    fun deleteTeacherComplaint(token: String)

    @Query("SELECT * FROM teacher_complaint WHERE id = :id")
    fun getComplaintMessageById(id: Int): Single<TeacherComplaintEntity>
    //endregion

}