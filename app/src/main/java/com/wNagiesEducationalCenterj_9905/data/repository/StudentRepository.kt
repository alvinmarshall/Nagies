package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiResponse
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.api.response.MessageResponse
import com.wNagiesEducationalCenterj_9905.api.response.ParentComplaintResponse
import com.wNagiesEducationalCenterj_9905.api.response.StudentProfileResponse
import com.wNagiesEducationalCenterj_9905.common.utils.ImagePathUtil
import com.wNagiesEducationalCenterj_9905.common.utils.RateLimiter
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.ComplaintDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.MessageDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.StudentDao
import com.wNagiesEducationalCenterj_9905.data.db.Entities.ComplaintEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.StudentProfileEntity
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val messageDao: MessageDao,
    private val db: AppDatabase,
    private val studentDao: StudentDao,
    private val complaintDao: ComplaintDao
) {
    private val studentRateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)
    fun fetchStudentMessages(token: String): LiveData<Resource<List<MessageEntity>>> {
        return object : NetworkBoundResource<List<MessageEntity>, MessageResponse>(appExecutors) {
            override fun loadFromDb(): LiveData<List<MessageEntity>> {
                return Transformations.switchMap(messageDao.getMessages()) { msg ->
                    if (msg == null) {
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
                } else {
                    db.runInTransaction {
                        messageDao.deleteMessages()
                        messageDao.insertMessages(item.messages)
                    }
                    Timber.i("status ${item.status}")
                }
            }

            override fun shouldFetch(data: List<MessageEntity>?): Boolean {
                val fetch = data == null || data.isEmpty() || studentRateLimiter.shouldFetch(token)
                Timber.i("should fetch data? $fetch")
                return data == null || data.isEmpty() || studentRateLimiter.shouldFetch(token)
            }

            override fun createCall() = apiService.getStudentMessages(token)
            override fun onFetchFailed() {
                studentRateLimiter.reset(token)
            }
        }.asLiveData()
    }

    fun getMessageById(message_id: Int): Single<MessageEntity> {
        return messageDao.getMessageById(message_id)
    }

    fun fetchStudentProfile(token: String): LiveData<Resource<StudentProfileEntity>> {
        return object : NetworkBoundResource<StudentProfileEntity, StudentProfileResponse>(appExecutors) {
            override fun saveCallResult(item: StudentProfileResponse) {
                if (item.status == 200) {
                    item.studentProfile.forEach { profile ->
                        profile.token = token
                        profile.imageUrl = ImagePathUtil.setCorrectPath(profile.imageUrl)
                    }
                    studentDao.insertStudentProfile(item.studentProfile[0])
                }
            }

            override fun shouldFetch(data: StudentProfileEntity?): Boolean = data == null

            override fun loadFromDb(): LiveData<StudentProfileEntity> {
                return Transformations.switchMap(studentDao.getStudentProfile(token)) { profile ->
                    if (profile == null) {
                        val data = MutableLiveData<StudentProfileEntity>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap studentDao.getStudentProfile(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<StudentProfileResponse>> =
                apiService.getStudentProfile(token)
        }.asLiveData()

    }

    fun sendParentComplaint(
        token: String,
        parentComplaintRequest: ParentComplaintRequest
    ): Single<ParentComplaintResponse> {
        return apiService.sendParentComplaint(token, parentComplaintRequest)
    }

    fun saveComplaintMessage(complaintEntity: ComplaintEntity): Single<Long> {
        return complaintDao.insertParentComplaint(complaintEntity)
    }

    fun getSavedParentComplaints(token: String): Flowable<List<ComplaintEntity>> {
        return complaintDao.getSavedComplaintMessage(token)
    }

    fun getSavedParentComplaintsById(id: Int): Single<ComplaintEntity> {
        return complaintDao.getSavedComplaintMessageById(id)
    }
}