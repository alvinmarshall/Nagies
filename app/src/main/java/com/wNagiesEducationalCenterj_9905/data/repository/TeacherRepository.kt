package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiResponse
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.request.ExplorerRequest
import com.wNagiesEducationalCenterj_9905.api.request.FileUploadRequest
import com.wNagiesEducationalCenterj_9905.api.request.TeacherMessageRequest
import com.wNagiesEducationalCenterj_9905.api.response.*
import com.wNagiesEducationalCenterj_9905.common.FetchType
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.common.utils.ServerPathUtil
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.AnnouncementDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.ComplaintDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.MessageDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.TeacherDao
import com.wNagiesEducationalCenterj_9905.data.db.Entities.*
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
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
    fun fetchAnnouncement(token: String, shouldFetch: Boolean = false,searchContent: String=""): LiveData<Resource<List<AnnouncementEntity>>> {
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
                return announcementDao.getAnnouncement(token,"%$searchContent%")
            }

            override fun createCall(): LiveData<ApiResponse<AnnouncementResponse>> =
                apiService.getTeachersAnnouncement(token)
        }.asLiveData()
    }

    fun fetchComplaint(
        token: String,
        shouldFetch: Boolean = false,
        searchContent: String = ""
    ): LiveData<Resource<List<ComplaintEntity>>> {
        return object : NetworkBoundResource<List<ComplaintEntity>, ComplaintResponse>(appExecutors) {
            override fun saveCallResult(item: ComplaintResponse) {
                if (item.status == 200) {
                    item.complaints.forEach { complaint ->
                        complaint.token = token
                    }
                    db.runInTransaction {
                        complaintDao.deleteComplaint(token)
                        complaintDao.insertComplaint(item.complaints)
                    }
                    preferenceProvider.setFetchDate(FetchType.COMPLAINT)
                }
            }

            override fun shouldFetch(data: List<ComplaintEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.COMPLAINT)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<ComplaintEntity>> {
                return complaintDao.getComplaintMessage(token, "%$searchContent%")
            }

            override fun createCall(): LiveData<ApiResponse<ComplaintResponse>> =
                apiService.getComplaint(token)
        }.asLiveData()
    }

    fun fetchTeacherProfile(token: String, shouldFetch: Boolean = false): LiveData<Resource<TeacherProfileEntity>> {
        return object : NetworkBoundResource<TeacherProfileEntity, TeacherProfileResponse>(appExecutors) {
            override fun saveCallResult(item: TeacherProfileResponse) {
                if (item.status == 200) {
                    item.teacherProfile.token = token
                    item.teacherProfile.imageUrl = ServerPathUtil.setCorrectPath(item.teacherProfile.imageUrl)
                    db.runInTransaction {
                        teacherDao.deleteTeacherProfile(token)
                        teacherDao.insertTeacherProfile(item.teacherProfile)
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

    fun getComplaintMessageById(id: Int): Single<ComplaintEntity> {
        return complaintDao.getComplaintMessageById(id)
    }

    fun getAnnouncementById(id: Int): Single<AnnouncementEntity> {
        return announcementDao.getAnnouncementById(id)
    }

    fun getSentMessages(token: String): Flowable<List<MessageEntity>> {
        return messageDao.getSentMessages(token)
    }

    fun uploadAssignmentPDF(token: String, request: MultipartBody.Part): Single<FileUploadResponse> {
        return apiService.uploadAssignmentPDF(token, request)
    }

    fun uploadAssignmentIMAGE(token: String, request: MultipartBody.Part): Single<FileUploadResponse> {
        return apiService.uploadAssignmentIMAGE(token, request)
    }

    fun fetchClassStudent(
        token: String,
        shouldFetch: Boolean = false,
        searchName: String = ""
    ): LiveData<Resource<List<ClassStudentEntity>>> {
        return object : NetworkBoundResource<List<ClassStudentEntity>, ClassStudentResponse>(appExecutors) {
            override fun saveCallResult(item: ClassStudentResponse) {
                if (item.status == 200) {
                    item.classStudent.forEach { student ->
                        student.token = token
                        student.imageUrl = ServerPathUtil.setCorrectPath(student.imageUrl)
                    }
                    db.runInTransaction {
                        teacherDao.deleteClassStudent(token)
                        teacherDao.insertClassStudent(item.classStudent)
                    }
                    preferenceProvider.setFetchDate(FetchType.CLASS_STUDENT)
                }
            }

            override fun shouldFetch(data: List<ClassStudentEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.COMPLAINT)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<ClassStudentEntity>> {
                return teacherDao.searchClassStudent(token, "%$searchName%")
            }

            override fun createCall(): LiveData<ApiResponse<ClassStudentResponse>> {
                return apiService.fetchClassStudents(token)
            }
        }.asLiveData()
    }

    fun uploadReportPDF(token: String, request: FileUploadRequest): Single<FileUploadResponse> {
        return apiService.uploadReportPDF(
            token,
            request.requestBody,
            request.studentInfo?.studentNo,
            request.studentInfo?.studentName
        )
    }

    fun uploadReportIMAGE(token: String, request: FileUploadRequest): Single<FileUploadResponse> {
        return apiService.uploadReportIMAGE(
            token,
            request.requestBody,
            request.studentInfo?.studentNo,
            request.studentInfo?.studentName
        )
    }

    fun fetchUploadData(token: String, request: ExplorerRequest): Observable<ExplorerResponse> {
        return apiService.getUploadedFile(token,request.type,request.format)
    }

    fun deleteUploadData(token: String, request: ExplorerRequest): Single<ExplorerDeleteResponse> {
        return apiService.deleteUploadedFile(token, request.id,request.type,request.format,request.path)
    }

}