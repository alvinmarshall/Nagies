package com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.wNagiesEducationalCenterj_9905.SessionManager
import com.wNagiesEducationalCenterj_9905.api.request.ChangePasswordRequest
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.repository.AuthRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.AuthResource
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
    private val preferenceProvider: PreferenceProvider
) : BaseViewModel() {
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun authenticatingParent(username: String, password: String): LiveData<Resource<UserEntity>> {
        return authRepository.authenticateParent(username, password)
    }

    fun authenticatingTeacher(username: String, password: String): LiveData<Resource<UserEntity>> {
        return authRepository.authenticateTeacher(username, password)
    }

    private fun queryAuthToken(token: String): LiveData<AuthResource<UserEntity>> {
        return LiveDataReactiveStreams.fromPublisher(authRepository.getAuthenticatedUserFromDb(token)
            .map {
                if (it.isEmpty()) return@map AuthResource.error("user not found", null)
                return@map AuthResource.authenticated(it[0])
            })
    }

    fun authenticateWithToken(token: String) {
        sessionManager.authenticateWith(queryAuthToken(token))
    }

    fun authCachedUserData(): LiveData<AuthResource<UserEntity>> = sessionManager.getCachedUser()

    fun changeAccountPassword(changePasswordRequest: ChangePasswordRequest) {
        disposable.addAll(Observable.just(preferenceProvider.getUserToken())
            .map { token ->
                return@map token
            }
            .flatMap { token ->
                authRepository.changeAccountPassword(token, changePasswordRequest)
            }

            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("data ${it.status}")
                if (it.status == 200) {
                    isSuccess.value = true
                    return@subscribe
                }

            }, {
                isSuccess.value = false
                Timber.i("change Password error: $it")
            })
        )
    }

    fun updateAccountPassword(newPass: String) {
        disposable.addAll(Single.just(preferenceProvider.getUserToken())
            .map { token ->
                return@map token
            }
            .flatMap { token ->
                authRepository.updateAccountPassword(token, newPass)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("update db password pass")
            }, {
                Timber.i("update db password error: $it")
            })
        )

    }
}