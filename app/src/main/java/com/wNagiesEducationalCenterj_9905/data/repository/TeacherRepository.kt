package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiResponse
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.request.TeacherMessageRequest
import com.wNagiesEducationalCenterj_9905.api.response.AnnouncementResponse
import com.wNagiesEducationalCenterj_9905.api.response.TeacherComplaintResponse
import com.wNagiesEducationalCenterj_9905.api.response.TeacherMessageResponse
import com.wNagiesEducationalCenterj_9905.api.response.TeacherProfileResponse
import com.wNagiesEducationalCenterj_9905.common.FetchType
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.common.utils.ServerPathUtil
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.AnnouncementDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.ComplaintDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.MessageDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.TeacherDao
import com.wNagiesEducationalCenterj_9905.data.db.Entities.AnnouncementEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherComplaintEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.TeacherProfileEntity
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class TeacherRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val teacherDao: TeacherDao,
    private val db: AppDatabase,
    private val announcementDao: AnnouncementDao,
    private val complaintDao: ComplaintDao,
    private val messageDao: MessageDao,
    private val preferenceProvider: PreferenceProvider
) {
    fun fetchAnnouncement(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<AnnouncementEntity>>> {
        return object : NetworkBoundResource<List<AnnouncementEntity>, AnnouncementResponse>(appExecutors) {
            override fun saveCallResult(item: AnnouncementResponse) {
                if (item.status == 200) {
                    item.messages.forEach { msg ->
                        msg.token = token
                    }
                    db.runInTransaction {
                        announcementDao.deleteAnnouncement(token)
                        announcementDao.insertAnnouncement(item.messages)
                    }
                    preferenceProvider.setFetchDate(FetchType.ANNOUNCEMENT)
                }

            }

            override fun shouldFetch(data: List<AnnouncementEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.ANNOUNCEMENT)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<AnnouncementEntity>> {
                return Transformations.switchMap(announcementDao.getAnnouncement(token)) { msg ->
                    if (msg.isEmpty()) {
                        val data = MutableLiveData<List<AnnouncementEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap announcementDao.getAnnouncement(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<AnnouncementResponse>> =
                apiService.getTeachersAnnouncement(token)
        }.asLiveData()
    }

    fun fetchComplaint(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<TeacherComplaintEntity>>> {
        return object : NetworkBoundResource<List<TeacherComplaintEntity>, TeacherComplaintResponse>(appExecutors) {
            override fun saveCallResult(item: TeacherComplaintResponse) {
                if (item.status == 200) {
                    item.complaints.forEach { complaint ->
                        complaint.token = token
                    }
                    db.runInTransaction {
                        complaintDao.deleteTeacherComplaint(token)
                        complaintDao.insertTeacherComplaint(item.complaints)
                    }
                    preferenceProvider.setFetchDate(FetchType.COMPLAINT)
                }
            }

            override fun shouldFetch(data: List<TeacherComplaintEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.COMPLAINT)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<TeacherComplaintEntity>> {
                return Transformations.switchMap(complaintDao.getTeacherComplaintMessage(token)) { complaint ->
                    if (complaint.isEmpty()) {
                        val data = MutableLiveData<List<TeacherComplaintEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap complaintDao.getTeacherComplaintMessage(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<TeacherComplaintResponse>> =
                apiService.getTeacherComplaint(token)
        }.asLiveData()
    }

    fun fetchTeacherProfile(token: String, shouldFetch: Boolean = false): LiveData<Resource<TeacherProfileEntity>> {
        return object : NetworkBoundResource<TeacherProfileEntity, TeacherProfileResponse>(appExecutors) {
            override fun saveCallResult(item: TeacherProfileResponse) {
                if (item.status == 200) {
                    item.teacherProfile.forEach { profile ->
                        profile.token = token
                        profile.imageUrl = ServerPathUtil.setCorrectPath(profile.imageUrl)
                    }
                    db.runInTransaction {
                        teacherDao.deleteTeacherProfile(token)
                        teacherDao.insertTecherProfile(item.teacherProfile[0])
                    }
                }
            }

            override fun shouldFetch(data: TeacherProfileEntity?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<TeacherProfileEntity> {
                return Transformations.switchMap(teacherDao.getTeacherProfile(token)) { profile ->
                    if (profile == null) {
                        val data = MutableLiveData<TeacherProfileEntity>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap teacherDao.getTeacherProfile(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<TeacherProfileResponse>> =
                apiService.getTeacherProfile(token)
        }.asLiveData()
    }

    fun sendTeacherMessage(
        token: String,
        teacherMessage: TeacherMessageRequest
    ): Single<TeacherMessageResponse> {
        return apiService.sendTeacherMessage(token, teacherMessage)
    }

    fun saveSentMessage(sentMessage: MessageEntity): Single<Long> {
        return messageDao.insertSentMessage(sentMessage)
    }

    fun getComplaintMessageById(id: Int): Single<TeacherComplaintEntity> {
        return complaintDao.getComplaintMessageById(id)
    }

    fun getAnnouncementById(id: Int): Single<AnnouncementEntity> {
        return announcementDao.getAnnouncementById(id)
    }

    fun getSentMessages(token: String): Flowable<List<MessageEntity>> {
        return messageDao.getSentMessages(token)
    }

}