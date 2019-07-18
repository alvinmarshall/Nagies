package com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wNagiesEducationalCenterj_9905.api.request.ParentComplaintRequest
import com.wNagiesEducationalCenterj_9905.common.extension.getCurrentDateTime
import com.wNagiesEducationalCenterj_9905.common.extension.toString
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.common.utils.ProfileLabel
import com.wNagiesEducationalCenterj_9905.data.db.Entities.*
import com.wNagiesEducationalCenterj_9905.data.repository.StudentRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.DownloadRequest
import com.wNagiesEducationalCenterj_9905.vo.Profile
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import java.io.*


class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val preferenceProvider: PreferenceProvider
) : BaseViewModel() {
    val cachedToken: MutableLiveData<String> = MutableLiveData()
    val cachedMessage: MutableLiveData<MessageEntity> = MutableLiveData()
    val cachedLabels: MutableLiveData<MutableList<Pair<Profile, String?>>> = MutableLiveData()
    var cachedSavedComplaint: MutableLiveData<Resource<List<ComplaintEntity>>> = MutableLiveData()
    val isSaved: MutableLiveData<Boolean> = MutableLiveData()
    val cachedSavedComplaintById: MutableLiveData<ComplaintEntity> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()

    fun getStudentMessages(token: String): LiveData<Resource<List<MessageEntity>>> {
        return studentRepository.fetchStudentMessages(token)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 200) {
                        savingComplaintToDb(parentComplaintRequest)
                        isSaved.value = true
                        return@subscribe
                    }
                    isSaved.value = false

                }, {
                    Timber.i("send error: $it")
                    errorMessage.value = com.wNagiesEducationalCenterj_9905.R.string.errorConnection
                })

        )
    }

    private fun savingComplaintToDb(parentComplaintRequest: ParentComplaintRequest) {
        disposable.addAll(
            Single.just(preferenceProvider.getUserToken())
                .map {
                    return@map it
                }
                .flatMap {
                    val date = getCurrentDateTime().toString("yyyy/MM/dd")
                    val complaint =
                        ComplaintEntity(parentComplaintRequest.content, date, it)
                    return@flatMap studentRepository.saveComplaintMessage(complaint)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.i("saved data id: $it")
                }, {})
        )
    }

    fun getSavedParentComplaint() {
        disposable.addAll(
            Flowable.just(preferenceProvider.getUserToken()).map {
                return@map it
            }
                .flatMap {
                    return@flatMap studentRepository.getSavedParentComplaints(it)
                }
                .map {
                    if (it.isEmpty()) return@map Resource.error("No content available", null)
                    return@map Resource.success(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cachedSavedComplaint.value = Resource.loading(null)
                    Timber.i("parent complaint messages: ${it.data?.size}")
                    cachedSavedComplaint.value = it
                }, {})
        )
    }

    fun getSavedParentComplaintById(complaint_id: Int) {
        disposable.addAll(
            studentRepository.getSavedParentComplaintsById(complaint_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    cachedSavedComplaintById.value = it
                }, {})
        )
    }

    fun downloadFilesFromServer(filePath: DownloadRequest, entityId: Int?, entity: String = "assignment") {
        disposable.addAll(
            Observable.just(preferenceProvider.getUserToken())
                .map {
                    return@map it
                }
                .flatMap {
                    return@flatMap studentRepository.fetchFileFromServer(it, filePath)
                }
                .flatMap {
                    saveToDisk(it)
                }
                .flatMap {
                    when (entity) {
                        "assignment" -> return@flatMap updateAssignmentEntityPath(it, entityId)
                        "report" -> return@flatMap updateReportEntityPath(it, entityId)
                        else -> return@flatMap null
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.i("row $it updated")

                }, { Timber.i(it, "get assignment err") })
        )
    }

    private fun updateAssignmentEntityPath(file: File, id: Int?): Observable<Int> {
        return Observable.create {
            id?.let { it1 ->
                val row = studentRepository.updateStudentAssignmentFilePath(it1, file.absolutePath)
                it.onNext(row)
            }
            it.onComplete()
        }
    }

    private fun saveToDisk(response: Response<ResponseBody>): Observable<File> {
        return Observable.create {
            try {
                val headerFileName = response.headers().get("Content-Disposition")
                val headerFileType = response.headers().get("Content-Type")
                val destinationFile: File?
                val filename = headerFileName?.replace("attachment; filename=", "")

                destinationFile = when (headerFileType) {
                    "image/jpeg" -> {
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename!!)
                    }
                    "image/png" -> {
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename!!)
                    }
                    "application/pdf" -> {
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename!!)
                    }
                    else -> null
                }
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    val file = File(destinationFile?.absolutePath!!)
                    if (!file.exists()) {
                        destinationFile.createNewFile()
                        Timber.i("file not created")
                        val bufferSink: BufferedSink = Okio.buffer(Okio.sink(destinationFile))
                        bufferSink.writeAll(response.body()?.source()!!)
                        bufferSink.close()
                        it.onNext(file)
                        it.onComplete()
                        Timber.i("file created")
                    } else {
                        it.onNext(file)
                        it.onComplete()
                        Timber.i("file already created")
                    }
                }
            } catch (e: IOException) {
                it.onError(e)
            }
        }
    }

    fun getStudentAssignmentPDF(token: String): LiveData<Resource<List<AssignmentEntity>>> {
        return studentRepository.fetchStudentAssignmentPDF(token)
    }

    fun getStudentAssignmentImage(token: String): LiveData<Resource<List<AssignmentEntity>>> {
        return studentRepository.fetchStudentAssignmentImage(token)
    }

    fun deleteAssignmentById(id: Int?, path: String?) {
        disposable.addAll(
            Observable.create<String> {
                id?.let { it1 -> studentRepository.deleteAssignmentById(it1) }
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

    fun getStudentReportPDF(token: String): LiveData<Resource<List<ReportEntity>>> {
        return studentRepository.fetchStudentReportPDF(token)
    }

    fun getStudentReportImage(token: String): LiveData<Resource<List<ReportEntity>>> {
        return studentRepository.fetchStudentReportImage(token)
    }

    private fun updateReportEntityPath(file: File, id: Int?): Observable<Int> {
        return Observable.create {
            id?.let { it1 ->
                val row = studentRepository.updateStudentReportFilePath(it1, file.absolutePath)
                it.onNext(row)
            }
            it.onComplete()
        }
    }

}