package com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wNagiesEducationalCenterj_9905.api.request.ChangePasswordRequest
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.common.UserAccount
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.repository.AuthRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferenceProvider: PreferenceProvider
) : BaseViewModel() {
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val account: MutableLiveData<UserAccount> = MutableLiveData()
    val cachedUser: MutableLiveData<Resource<UserEntity>> = MutableLiveData()

    fun authenticatingParent(username: String, password: String): LiveData<Resource<UserEntity>> {
        return authRepository.authenticateParent(username, password)
    }

    fun authenticatingTeacher(username: String, password: String): LiveData<Resource<UserEntity>> {
        return authRepository.authenticateTeacher(username, password)
    }

    fun changeAccountPassword(changePasswordRequest: ChangePasswordRequest, userAccount: UserAccount) {
        disposable.addAll(Observable.just(preferenceProvider.getUserToken())
            .map { token ->
                return@map token
            }
            .flatMap { token ->
                authRepository.changeAccountPassword(token, changePasswordRequest, userAccount)
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

    fun getUserAccount() {
        disposable.addAll(Single.just(preferenceProvider.getUserLoginRole())
            .map { role ->
                when (role) {
                    LOGIN_ROLE_OPTIONS[0] -> return@map UserAccount.PARENT
                    else -> return@map UserAccount.TEACHER
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                account.value = it
            }, {})
        )
    }

    fun authenticateWithToken() {
        disposable.addAll(Single.just(preferenceProvider.getUserToken())
            .map { return@map it }
            .flatMap { token ->
                authRepository.getAuthenticatedUserFromDb(token)
            }
            .map {
                if (it.id == 0) {
                    return@map Resource.error("user not found", null)
                }
                return@map Resource.success(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                cachedUser.value = it
            }, { err -> Timber.i(err) })
        )

    }
}