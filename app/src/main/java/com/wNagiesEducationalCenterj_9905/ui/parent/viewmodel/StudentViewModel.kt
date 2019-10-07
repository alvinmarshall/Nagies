package com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.common.DBEntities
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.common.utils.ProfileLabel
import com.wNagiesEducationalCenterj_9905.data.db.Entities.*
import com.wNagiesEducationalCenterj_9905.data.repository.StudentRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.Profile
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val preferenceProvider: PreferenceProvider
) : BaseViewModel() {
    val cachedToken: MutableLiveData<String> = MutableLiveData()
    val cachedMessage: MutableLiveData<MessageEntity> = MutableLiveData()
    val cachedLabels: MutableLiveData<MutableList<Pair<Profile, String?>>> = MutableLiveData()
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val cachedSavedComplaintById: MutableLiveData<ComplaintEntity> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    var cachedAnnouncement: MutableLiveData<AnnouncementEntity> = MutableLiveData()
    val searchString: MutableLiveData<String> = MutableLiveData()


    fun getStudentMessages(
        token: String,
        shouldFetch: Boolean = false,
        search: String = ""
    ): LiveData<Resource<List<MessageEntity>>> {
        return studentRepository.fetchStudentMessages(token, shouldFetch, search)
    }

    fun getUserToken() {
        disposable.addAll(
            Flowable.just(preferenceProvider.getUserToken())
                .map {
                    return@map it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cachedToken.value = it
                    Timber.i("token: $it")
                }, {})
        )
    }

    fun getMessageById(messageId: Int) {
        disposable.addAll(
            studentRepository.getMessageById(messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ cachedMessage.value = it }, {})
        )

    }

    fun getStudentProfile(token: String): LiveData<Resource<StudentProfileEntity>> {
        return studentRepository.fetchStudentProfile(token)
    }

    fun setProfileLabels(data: StudentProfileEntity?) {
        disposable.addAll(
            Observable.create<MutableList<Pair<Profile, String?>>> {
                val myList = mutableListOf<Pair<Profile, String?>>()
                val profileData = arrayListOf(
                    data?.studentNo, data?.studentName, data?.dob,
                    data?.gender, data?.admissionDate, data?.section,
                    data?.semester, data?.level, data?.guardian,
                    data?.contact, data?.faculty, data?.index
                )
                val label = ArrayList<String>()
                val drawable = ArrayList<Int>()
                for (student in ProfileLabel.getMultiple()) {
                    label.add(student.first)
                    drawable.add(student.second)
                }

                for (i in profileData.indices) {
                    myList.add(Pair(Profile(label[i], drawable[i]), profileData[i]))
                }
                it.onNext(myList)
                it.onComplete()

            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cachedLabels.value = it
                }, {})
        )
    }

    fun sendComplaint(parentComplaintRequest: ParentComplaintRequest) {
        disposable.addAll(
            Single.just(preferenceProvider.getUserToken())
                .map {
                    Timber.i("single token $it")
                    return@map it
                }
                .flatMap {
                    studentRepository.sendParentComplaint(it, parentComplaintRequest)
                }
                .doOnError { isSuccess.postValue(false) }
                .doOnSuccess { isSuccess.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.i("comp $it")
                    if (it.status == 200) {
                        Timber.i("message sent $it")
                    }

                }, {
                    Timber.i("send error: $it")
                    errorMessage.value = com.wNagiesEducationalCenterj_9905.R.string.errorConnection
                })

        )
    }


    fun getParentComplaintById(complaint_id: Int) {
        disposable.addAll(
            studentRepository.getComplaintMessageById(complaint_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cachedSavedComplaintById.value = it
                }, {})
        )
    }

    fun getComplaintMessage(
        token: String,
        shouldFetch: Boolean=false,
        searchContent: String = ""
    ): LiveData<Resource<List<ComplaintEntity>>> {
        return studentRepository.fetchSentComplaint(token, shouldFetch, searchContent)
    }

    fun getStudentAssignmentPDF(
        token: String,
        shouldFetch: Boolean = false
    ): LiveData<Resource<List<AssignmentEntity>>> {
        return studentRepository.fetchStudentAssignmentPDF(token, shouldFetch)
    }

    fun getStudentAssignmentImage(
        token: String,
        shouldFetch: Boolean = false
    ): LiveData<Resource<List<AssignmentEntity>>> {
        return studentRepository.fetchStudentAssignmentImage(token, shouldFetch)
    }

    fun deleteFileById(id: Int?, path: String?, entity: DBEntities) {
        disposable.addAll(
            Observable.create<String> {
                when (entity) {
                    DBEntities.ASSIGNMENT -> {
                        id?.let { it1 -> studentRepository.deleteAssignmentById(it1) }
                    }
                    DBEntities.REPORT -> {
                        id?.let { it1 -> studentRepository.deleteReportById(it1) }
                    }
                    DBEntities.CIRCULAR -> {
                    }
                    DBEntities.BILLING -> {
                        id?.let { it1 -> studentRepository.deleteBillingById(it1) }
                    }
                    DBEntities.TIME_TABLE -> {
                    }
                }
                path?.let { p ->
                    val file = File(p)
                    if (file.exists()) {
                        file.delete()
                    }
                }
                it.onNext("finish")
                it.onComplete()
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.i("delete $it")
                }, { Timber.i(it) })
        )
    }

    fun getStudentReportPDF(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<ReportEntity>>> {
        return studentRepository.fetchStudentReportPDF(token, shouldFetch)
    }

    fun getStudentReportImage(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<ReportEntity>>> {
        return studentRepository.fetchStudentReportImage(token, shouldFetch)
    }

    fun saveDownloadFilePathToDb(id: Int?, path: String?, entity: DBEntities) {
        disposable.addAll(
            Observable.fromCallable {
                when (entity) {
                    DBEntities.ASSIGNMENT -> {
                        id?.let { path?.let { it1 -> studentRepository.updateStudentAssignmentFilePath(it, it1) } }
                    }
                    DBEntities.REPORT -> {
                        id?.let { path?.let { it1 -> studentRepository.updateStudentReportFilePath(it, it1) } }
                    }
                    DBEntities.CIRCULAR -> {
                        id?.let { path?.let { it1 -> studentRepository.updateCircularFilePath(it, it1) } }
                    }
                    DBEntities.BILLING -> {
                        id?.let { path?.let { it1 -> studentRepository.updateBillingFilePath(it, it1) } }
                    }
                    DBEntities.TIME_TABLE -> {
                        id?.let { path?.let { it1 -> studentRepository.updateTimetableFilePath(it, it1) } }
                    }
                }
            }
                .doOnComplete { isSuccess.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.i("data is saved to db $it")
                }, {})
        )

    }

    fun getClassTeacher(token: String, searchName: String = ""): LiveData<Resource<List<StudentTeacherEntity>>> {
        return studentRepository.getClassTeacher(token, search = searchName)
    }

    fun getCircularInformation(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<CircularEntity>>> {
        return studentRepository.fetchCircular(token, shouldFetch)
    }

    fun getStudentBill(token: String, shouldFetch: Boolean = false): LiveData<Resource<List<BillingEntity>>> {
        return studentRepository.fetchStudentBills(token, shouldFetch)
    }

    fun getAnnouncementMessage(
        token: String,
        shouldFetch: Boolean = false,
        searchContent: String = ""
    ): LiveData<Resource<List<AnnouncementEntity>>> {
        return studentRepository.fetchStudentAnnouncement(token, shouldFetch, searchContent)
    }

    fun getAnnouncementById(id: Int) {
        disposable.addAll(
            studentRepository.getAnnouncementById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cachedAnnouncement.value = it
                }, { err -> Timber.i(err) })
        )

    }

    fun getTimetable(token: String): LiveData<Resource<List<TimeTableEntity>>> {
        return studentRepository.fetchStudentTimetable(token)
    }

    fun deleteMessage(id: Int?){
        disposable.addAll(Single.just(preferenceProvider.getUserToken())
            .map { return@map it }
            .flatMap { studentRepository.deleteComplaint(it,id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.status == 200){
                    Timber.i("message deleted success")
                }
            },{err-> Timber.i(err,"deleteMessage")}))
    }


}