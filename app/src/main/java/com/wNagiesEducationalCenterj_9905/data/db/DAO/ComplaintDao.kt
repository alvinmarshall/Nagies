package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ComplaintEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ComplaintDao {
    @Insert
    fun insertParentComplaint(complaintEntity: ComplaintEntity):Single<Long>

    @Query("SELECT * FROM parent_complaint WHERE token = :token ORDER BY id DESC")
    fun getSavedComplaintMessage(token:String):Flowable<List<ComplaintEntity>>

    @Query("SELECT * FROM parent_complaint WHERE id = :id")
    fun getSavedComplaintMessageById(id:Int):Single<ComplaintEntity>
}