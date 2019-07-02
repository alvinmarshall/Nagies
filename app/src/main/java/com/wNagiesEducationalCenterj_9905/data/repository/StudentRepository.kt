package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.response.MessageResponse
import com.wNagiesEducationalCenterj_9905.common.utils.RateLimiter
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.MessageDao
import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val messageDao: MessageDao,
    private val db:AppDatabase
) {
    private val studentRateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)
    fun fetchStudentMessages(token:String): LiveData<Resource<List<MessageEntity>>> {
        return object : NetworkBoundResource<List<MessageEntity>, MessageResponse>(appExecutors) {
            override fun loadFromDb(): LiveData<List<MessageEntity>> {
                return Transformations.switchMap(messageDao.getMessages()){msg ->
                    if (msg == null){
                        val data = MutableLiveData<List<MessageEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap messageDao.getMessages()
                }
            }

            override fun saveCallResult(item: MessageResponse) {
                if (item.status == 200) {
                    item.messages.forEach { msg ->
                        msg.token = token
                    }
                    db.runInTransaction {
                        messageDao.deleteMessages()
                        messageDao.insertMessages(item.messages)
                    }
                }else{
                    db.runInTransaction {
                        messageDao.deleteMessages()
                        messageDao.insertMessages(item.messages)
                    }
                    Timber.i("status ${item.status}")
                }
            }


            override fun shouldFetch(data: List<MessageEntity>?): Boolean {
                return data == null || data.isEmpty() || studentRateLimiter.shouldFetch(token)
            }

            override fun createCall() = apiService.getStudentMessages(token)
            override fun onFetchFailed() {
                studentRateLimiter.reset(token)
            }
        }.asLiveData()
    }

    fun getMessageById(message_id:Int):Single<MessageEntity>{
        return messageDao.getMessageById(message_id)
    }
}