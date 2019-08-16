package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiResponse
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.api.response.*
import com.wNagiesEducationalCenterj_9905.common.FetchType
import com.wNagiesEducationalCenterj_9905.common.IMAGE_FORMAT
import com.wNagiesEducationalCenterj_9905.common.PDF_FORMAT
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.common.utils.ServerPathUtil
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.*
import com.wNagiesEducationalCenterj_9905.data.db.Entities.*
import com.wNagiesEducationalCenterj_9905.vo.DownloadRequest
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val messageDao: MessageDao,
    private val db: AppDatabase,
    private val studentDao: StudentDao,
    private val complaintDao: ComplaintDao,
    private val assignmentDao: AssignmentDao,
    private val reportDao: ReportDao,
    private val preferenceProvider: PreferenceProvider
) {
    fun fetchStudentMessages(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<MessageEntity>>> {
        return object : NetworkBoundResource<List<MessageEntity>, MessageResponse>(appExecutors) {
            override fun loadFromDb(): LiveData<List<MessageEntity>> {
                return Transformations.switchMap(messageDao.getMessages(token)) { msg ->
                    if (msg == null) {
                        val data = MutableLiveData<List<MessageEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap messageDao.getMessages(token)
                }
            }

            override fun saveCallResult(item: MessageResponse) {
                if (item.status == 200) {
                    item.messages.forEach { msg ->
                        msg.token = token
                    }
                    db.runInTransaction {
                        messageDao.deleteMessages(token)
                        messageDao.insertMessages(item.messages)
                    }
                    preferenceProvider.setFetchDate(FetchType.MESSAGE)
                }
            }

            override fun shouldFetch(data: List<MessageEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.MESSAGE)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun createCall() = apiService.getStudentMessages(token)
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
                        profile.imageUrl = ServerPathUtil.setCorrectPath(profile.imageUrl)
                    }
                    db.runInTransaction {
                        studentDao.deleteProfile(token)
                        studentDao.insertStudentProfile(item.studentProfile[0])
                    }
                }
            }

            override fun shouldFetch(data: StudentProfileEntity?): Boolean {
                return data == null
            }

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

    fun fetchFileFromServer(token: String, url: DownloadRequest): Observable<Response<ResponseBody>> {
        return apiService.getFilesFromServer(token, url)
    }

    fun fetchStudentAssignmentPDF(
        token: String,
        shouldFetch: Boolean = false
    ): LiveData<Resource<List<AssignmentEntity>>> {
        return object : NetworkBoundResource<List<AssignmentEntity>, AssignmentResponse>(appExecutors) {
            override fun saveCallResult(item: AssignmentResponse) {
                if (item.status == 200) {
                    item.assignment.forEach { pdf ->
                        pdf.format = "pdf"
                        pdf.token = token
                    }
                    db.runInTransaction {
                        assignmentDao.deleteAssignmentPDF(token)
                        assignmentDao.insertAssignment(item.assignment)
                    }
                    preferenceProvider.setFetchDate(FetchType.ASSIGNMENT_PDF)
                }
            }

            override fun shouldFetch(data: List<AssignmentEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.ASSIGNMENT_PDF)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<AssignmentEntity>> {
                return Transformations.switchMap(assignmentDao.getStudentAssignmentPDF(token)) { pdf ->
                    if (pdf == null) {
                        val data = MutableLiveData<List<AssignmentEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap assignmentDao.getStudentAssignmentPDF(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<AssignmentResponse>> {
                return apiService.getStudentAssignmentPDF(token)
            }
        }.asLiveData()
    }

    fun fetchStudentAssignmentImage(
        token: String,
        shouldFetch: Boolean = false
    ): LiveData<Resource<List<AssignmentEntity>>> {
        return object : NetworkBoundResource<List<AssignmentEntity>, AssignmentResponse>(appExecutors) {
            override fun saveCallResult(item: AssignmentResponse) {
                if (item.status == 200) {
                    item.assignment.forEach { image ->
                        image.token = token
                        image.format = "image"
                    }
                    db.runInTransaction {
                        assignmentDao.deleteAssignmentImage(token)
                        assignmentDao.insertAssignment(item.assignment)
                    }
                    preferenceProvider.setFetchDate(FetchType.ASSIGNMENT_IMAGE)
                }
            }

            override fun shouldFetch(data: List<AssignmentEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.ASSIGNMENT_IMAGE)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<AssignmentEntity>> {
                return Transformations.switchMap(assignmentDao.getAssignmentImage(token)) { image ->
                    if (image.isEmpty()) {
                        val data = MutableLiveData<List<AssignmentEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap assignmentDao.getAssignmentImage(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<AssignmentResponse>> {
                return apiService.getStudentAssignmentImage(token)
            }
        }.asLiveData()
    }

    fun updateStudentAssignmentFilePath(id: Int, path: String): Int {
        return assignmentDao.updateAssignmentPath(path, id)
    }

    fun deleteAssignmentById(id: Int) {
        return assignmentDao.deleteAssignmentById(id)
    }

    fun deleteReportById(id: Int) {
        return reportDao.deleteReportById(id)
    }

    fun fetchStudentReportPDF(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<ReportEntity>>> {
        return object : NetworkBoundResource<List<ReportEntity>, ReportResponse>(appExecutors) {
            override fun saveCallResult(item: ReportResponse) {
                if (item.status == 200) {
                    item.report.forEach { pdf ->
                        pdf.format = PDF_FORMAT
                        pdf.token = token
                    }
                    db.runInTransaction {
                        reportDao.deleteReportPDF(token)
                        reportDao.insertReport(item.report)
                    }
                    preferenceProvider.setFetchDate(FetchType.REPORT_PDF)
                }
            }

            override fun shouldFetch(data: List<ReportEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.REPORT_PDF)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<ReportEntity>> {
                return Transformations.switchMap(reportDao.getStudentReportPDF(token)) { pdf ->
                    if (pdf.isEmpty()) {
                        val data = MutableLiveData<List<ReportEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap reportDao.getStudentReportPDF(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<ReportResponse>> {
                return apiService.getStudentReportPDF(token)
            }
        }.asLiveData()
    }

    fun fetchStudentReportImage(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<ReportEntity>>> {
        return object : NetworkBoundResource<List<ReportEntity>, ReportResponse>(appExecutors) {
            override fun saveCallResult(item: ReportResponse) {
                if (item.status == 200) {
                    item.report.forEach { image ->
                        image.token = token
                        image.format = IMAGE_FORMAT
                    }
                    db.runInTransaction {
                        reportDao.deleteReportImage(token)
                        reportDao.insertReport(item.report)
                    }
                    preferenceProvider.setFetchDate(FetchType.REPORT_IMAGE)
                }
            }

            override fun shouldFetch(data: List<ReportEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.REPORT_IMAGE)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<ReportEntity>> {
                return Transformations.switchMap(reportDao.getStudentReportImage(token)) { image ->
                    if (image.isEmpty()) {
                        val data = MutableLiveData<List<ReportEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap reportDao.getStudentReportImage(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<ReportResponse>> {
                return apiService.getStudentReportImage(token)
            }
        }.asLiveData()
    }

    fun updateStudentReportFilePath(id: Int, path: String): Int {
        return reportDao.updateReportPath(path, id)
    }

    fun getClassTeacher(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<StudentTeacherEntity>>> {
        return object : NetworkBoundResource<List<StudentTeacherEntity>, StudentTeachersResponse>(appExecutors) {
            override fun saveCallResult(item: StudentTeachersResponse) {
                if (item.status == 200) {
                    item.studentTeachers.forEach { teacher ->
                        teacher.imageUrl = ServerPathUtil.setCorrectPath(teacher.imageUrl)
                        teacher.token = token
                    }
                    db.runInTransaction {
                        studentDao.deleteClassTeacher(token)
                        studentDao.insertStudentTeacher(item.studentTeachers)
                    }
                    preferenceProvider.setFetchDate(FetchType.CLASSTEACHER)
                }
            }

            override fun shouldFetch(data: List<StudentTeacherEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.CLASSTEACHER)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<StudentTeacherEntity>> {
                return Transformations.switchMap(studentDao.getClassTeacher(token)) { teacher ->
                    if (teacher == null) {
                        val data: MutableLiveData<List<StudentTeacherEntity>> = MutableLiveData()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap studentDao.getClassTeacher(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<StudentTeachersResponse>> =
                apiService.getClassTeacher(token)
        }.asLiveData()

    }
}