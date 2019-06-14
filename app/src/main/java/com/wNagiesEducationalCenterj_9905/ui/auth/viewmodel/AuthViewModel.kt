package com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.wNagiesEducationalCenterj_9905.SessionManager
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.repository.AuthRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.AuthResource
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {
    val cachedUser:MediatorLiveData<AuthResource<UserEntity>> = MediatorLiveData()

    fun authenticatingParent(username: String, password: String): LiveData<Resource<UserEntity>> {
        return authRepository.authenticateUser(username, password)
    }

    private fun queryAuthUUID(uuid:String):LiveData<AuthResource<UserEntity>>{
        return LiveDataReactiveStreams.fromPublisher(authRepository.getAuthenticatedUserFromDb(uuid)
            .map {
                if (it.isEmpty()) return@map AuthResource.error("user not found",null)
                return@map AuthResource.authenticated(it[0])
        })

    }

    fun authenticateWithUUID(uuid:String){
        sessionManager.authenticateWith(queryAuthUUID(uuid))
    }

    fun authCachedUserData():LiveData<AuthResource<UserEntity>> = sessionManager.getCachedUser()

}