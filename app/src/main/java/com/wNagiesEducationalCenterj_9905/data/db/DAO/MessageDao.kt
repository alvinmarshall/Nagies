package com.wNagiesEducationalCenterj_9905.data.db.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessages(messageEntity: List<MessageEntity>)

    @Query("SELECT * FROM messages WHERE token = :token AND content LIKE :search ORDER BY id DESC")
    fun getMessages(token: String,search:String):LiveData<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE token = :token LIMIT 1")
    fun isMessageResent(token:String):Flowable<List<MessageEntity>>

    @Query("DELETE FROM messages WHERE token = :token")
    fun deleteMessages(token: String)

    @Query("SELECT * FROM messages WHERE id = :message_id")
    fun getMessageById(message_id:Int):Single<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSentMessage(messageEntity: MessageEntity):Single<Long>

    @Query("SELECT * FROM messages WHERE token = :token")
    fun getSentMessages(token: String):Flowable<List<MessageEntity>>

    @Query("DELETE FROM messages WHERE uid = :id")
    fun deleteMessageById(id:Int)
}