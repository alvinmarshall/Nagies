package com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity
import com.wNagiesEducationalCenterj_9905.data.repository.StudentRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import javax.inject.Inject

class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val preferenceProvider: PreferenceProvider
) : BaseViewModel() {
    val cachedToken: MutableLiveData<String> = MutableLiveData()
    val cachedMessage: MutableLiveData<MessageEntity> = MutableLiveData()
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


}