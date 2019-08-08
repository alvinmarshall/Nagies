package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.AnnouncementEntity
import io.reactivex.Single

@Dao
interface AnnouncementDao {
    @Insert
    fun insertAnnouncement(messageList: List<AnnouncementEntity>)

    @Query("DELETE FROM announcement WHERE token = :token")
    fun deleteAnnouncement(token: String)

    @Query("SELECT * FROM announcement WHERE token = :token")
    fun getAnnouncement(token: String): LiveData<List<AnnouncementEntity>>

    @Query("SELECT * FROM announcement WHERE id = :announcement_id")
    fun getAnnouncementById(announcement_id: Int): Single<AnnouncementEntity>

}