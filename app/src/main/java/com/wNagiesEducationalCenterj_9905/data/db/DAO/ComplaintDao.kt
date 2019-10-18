package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ComplaintEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ComplaintDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComplaint(ComplaintEntityList: List<ComplaintEntity>)

    @Query("SELECT * FROM complaint WHERE token = :token AND message LIKE :search")
    fun getComplaintMessage(token: String,search:String): LiveData<List<ComplaintEntity>>

    @Query("SELECT * FROM complaint WHERE id = :id")
    fun getComplaintMessageById(id: Int): Single<ComplaintEntity>

    @Query("DELETE FROM complaint WHERE token = :token")
    fun deleteComplaint(token: String)

    @Query("DELETE FROM complaint WHERE uid = :id")
    fun deleteComplaintById(id:Int)
}