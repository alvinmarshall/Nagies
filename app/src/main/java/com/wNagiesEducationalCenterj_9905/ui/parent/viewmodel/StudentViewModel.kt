package com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.common.utils.ProfileLabel
import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.StudentProfileEntity
import com.wNagiesEducationalCenterj_9905.data.repository.StudentRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.Profile
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val preferenceProvider: PreferenceProvider
) : BaseViewModel() {
    val cachedToken: MutableLiveData<String> = MutableLiveData()
    val cachedMessage: MutableLiveData<MessageEntity> = MutableLiveData()
    val cachedLabels:MutableLiveData<MutableList<Pair<Profile,String?>>> = MutableLiveData()
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
            Completable.complete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val myList = mutableListOf<Pair<Profile,String?>>()
                    val profileData = arrayListOf(
                        data?.studentNo,data?.studentName,data?.dob,
                        data?.gender,data?.admissionDate,data?.section,
                        data?.semester,data?.level,data?.guardian,
                        data?.contact,data?.faculty,data?.index
                    )
                    val label = ArrayList<String>()
                    val drawable = ArrayList<Int>()
                    for (student in ProfileLabel.getMultiple()  ){
                        label.add(student.first)
                        drawable.add(student.second)
                    }

                    for (i in profileData.indices){
                        myList.add(Pair(Profile(label[i],drawable[i]),profileData[i]))
                    }
                    cachedLabels.value = myList
                },{})
        )
    }


}