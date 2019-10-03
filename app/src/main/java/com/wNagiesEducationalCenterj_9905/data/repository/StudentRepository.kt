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
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Single
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
    private val preferenceProvider: PreferenceProvider,
    private val announcementDao: AnnouncementDao
) {
    fun fetchStudentMessages(
        token: String,
        shouldFetch: Boolean = false,
        searchContent: String = ""
    ): LiveData<Resource<List<MessageEntity>>> {
        return object : NetworkBoundResource<List<MessageEntity>, MessageResponse>(appExecutors) {
            override fun loadFromDb(): LiveData<List<MessageEntity>> {
                return messageDao.getMessages(token, "%$searchContent%")
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

    fun getComplaintMessageById(id: Int): Single<ComplaintEntity> {
        return complaintDao.getComplaintMessageById(id)
    }

    fun fetchStudentProfile(token: String): LiveData<Resource<StudentProfileEntity>> {
        return object : NetworkBoundResource<StudentProfileEntity, StudentProfileResponse>(appExecutors) {
            override fun saveCallResult(item: StudentProfileResponse) {
                if (item.status == 200) {
                    item.studentProfile.token = token
                    item.studentProfile.imageUrl = ServerPathUtil.setCorrectPath(item.studentProfile.imageUrl)
                    db.runInTransaction {
                        studentDao.deleteProfile(token)
                        studentDao.insertStudentProfile(item.studentProfile)
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


    fun fetchSentComplaint(
        token: String,
        shouldFetch: Boolean = false,
        searchContent: String = ""
    ): LiveData<Resource<List<ComplaintEntity>>> {
        return object : NetworkBoundResource<List<ComplaintEntity>, ComplaintResponse>(appExecutors) {
            override fun saveCallResult(item: ComplaintResponse) {
                if (item.status == 200){
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
                return complaintDao.getComplaintMessage(token,"%$searchContent%")
            }

            override fun createCall(): LiveData<ApiResponse<ComplaintResponse>> {
                return apiService.getComplaint(token)
            }
        }.asLiveData()
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
                        pdf.fileUrl = ServerPathUtil.setCorrectPath(pdf.fileUrl)
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
                        image.fileUrl = ServerPathUtil.setCorrectPath(image.fileUrl)
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
                        pdf.fileUrl = ServerPathUtil.setCorrectPath(pdf.fileUrl)
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
                Timber.i("data: ${item.count}")
                if (item.status == 200) {
                    item.report.forEach { image ->
                        image.token = token
                        image.format = IMAGE_FORMAT
                        image.fileUrl = ServerPathUtil.setCorrectPath(image.fileUrl)
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

    fun getClassTeacher(
        token: String,
        shouldFetch: Boolean = false,
        search: String = ""
    ): LiveData<Resource<List<StudentTeacherEntity>>> {
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
                    preferenceProvider.setFetchDate(FetchType.CLASS_TEACHER)
                }
            }

            override fun shouldFetch(data: List<StudentTeacherEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.CLASS_TEACHER)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<StudentTeacherEntity>> {
                return studentDao.getClassTeacher(token, "%$search%")
            }

            override fun createCall(): LiveData<ApiResponse<StudentTeachersResponse>> =
                apiService.getClassTeacher(token)
        }.asLiveData()

    }

    fun fetchCircular(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<CircularEntity>>> {
        return object : NetworkBoundResource<List<CircularEntity>, CircularResponse>(appExecutors) {
            override fun saveCallResult(item: CircularResponse) {
                if (item.status == 200) {
                    item.circular.forEach { circular ->
                        circular.token = token
                        circular.path = circular.fileUrl
                        circular.fileUrl = ServerPathUtil.setCorrectPath(circular.fileUrl)
                    }
                    db.runInTransaction {
                        studentDao.deleteCircular(token)
                        studentDao.insertCircular(item.circular)
                    }
                    preferenceProvider.setFetchDate(FetchType.CIRCULAR)
                }
            }

            override fun shouldFetch(data: List<CircularEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.CIRCULAR)
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<CircularEntity>> {
                return Transformations.switchMap(studentDao.getCircular(token)) { circular ->
                    if (circular.isEmpty()) {
                        val data = MutableLiveData<List<CircularEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap studentDao.getCircular(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<CircularResponse>> = apiService.getCircular(token)
        }.asLiveData()
    }

    fun updateCircularFilePath(id: Int, path: String): Int {
        return studentDao.updateCircularImagePath(id, path)
    }

    fun fetchStudentBills(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<BillingEntity>>> {
        return object : NetworkBoundResource<List<BillingEntity>, BillingResponse>(appExecutors) {
            override fun saveCallResult(item: BillingResponse) {
                if (item.status == 200) {
                    item.billing.forEach { bill ->
                        bill.token = token
                        bill.format = IMAGE_FORMAT
                        bill.fileUrl = ServerPathUtil.setCorrectPath(bill.fileUrl)
                    }
                    db.runInTransaction {
                        studentDao.deleteStudentBill(token)
                        studentDao.insertStudentBills(item.billing)
                    }
                    preferenceProvider.setFetchDate(FetchType.BILLING)
                }
            }

            override fun shouldFetch(data: List<BillingEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.BILLING)
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<BillingEntity>> {
                return Transformations.switchMap(studentDao.getStudentBills(token)) { bills ->
                    if (bills.isEmpty()) {
                        val data = MutableLiveData<List<BillingEntity>>()
                        data.postValue(null)
                        return@switchMap data
                    }
                    return@switchMap studentDao.getStudentBills(token)
                }
            }

            override fun createCall(): LiveData<ApiResponse<BillingResponse>> = apiService.getBilling(token)

        }.asLiveData()
    }

    fun updateBillingFilePath(id: Int, path: String): Int {
        return studentDao.updateBillingImagePath(id, path)
    }

    fun updateTimetableFilePath(id: Int, path: String): Int {
        return studentDao.updateBillingImagePath(id, path)
    }

    fun deleteBillingById(id: Int) {
        return studentDao.deleteBillingById(id)
    }

    fun fetchStudentAnnouncement(
        token: String,
        shouldFetch: Boolean = false, searchContent: String = ""
    ): LiveData<Resource<List<AnnouncementEntity>>> {
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
                return announcementDao.getAnnouncement(token, searchContent)
            }

            override fun createCall(): LiveData<ApiResponse<AnnouncementResponse>> =
                apiService.getStudentAnnouncement(token)
        }.asLiveData()
    }

    fun getAnnouncementById(id: Int): Single<AnnouncementEntity> {
        return announcementDao.getAnnouncementById(id)
    }

    fun fetchStudentTimetable(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<TimeTableEntity>>> {
        return object : NetworkBoundResource<List<TimeTableEntity>, TimetableResponse>(appExecutors) {
            override fun saveCallResult(item: TimetableResponse) {
                if (item.status == 200) {
                    item.timetable.forEach { table ->
                        table.token = token
                        table.fileUrl = ServerPathUtil.setCorrectPath(table.fileUrl)
                    }
                    db.runInTransaction {
                        studentDao.deleteTimetable(token)
                        studentDao.insertTimetable(item.timetable)
                    }
                    preferenceProvider.setFetchDate(FetchType.TIME_TABLE)
                }
            }

            override fun shouldFetch(data: List<TimeTableEntity>?): Boolean {
                val isOld = preferenceProvider.getFetchType(FetchType.TIME_TABLE)
                Timber.i("is old $isOld")
                return data == null || data.isEmpty() || shouldFetch || isOld
            }

            override fun loadFromDb(): LiveData<List<TimeTableEntity>> {
                return studentDao.getStudentTimetable(token)
            }

            override fun createCall(): LiveData<ApiResponse<TimetableResponse>> {
                return apiService.fetchStudentTimetable(token)
            }
        }.asLiveData()
    }

    fun deleteComplaint(token:String,id:Int?,type: String="complaint"):Single<DeleteMessageResponse>{
        id?.let { complaintDao.deleteComplaintById(it) }
        return apiService.deleteMessage(token,id,type)
    }
}