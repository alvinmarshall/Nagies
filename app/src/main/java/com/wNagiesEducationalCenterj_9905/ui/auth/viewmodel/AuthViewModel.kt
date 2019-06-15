package com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.wNagiesEducationalCenterj_9905.SessionManager
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.repository.AuthRepository
import com.wNagiesEducationalCenterj_9905.viewmodel.BaseViewModel
import com.wNagiesEducationalCenterj_9905.vo.AuthResource
import com.wNagiesEducationalCenterj_9905.vo.Resource
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : BaseViewModel() {
    fun authenticatingParent(username: String, password: String): LiveData<Resource<UserEntity>> {
        return authRepository.authenticateParent(username, password)
    }

    fun authenticatingTeacher(username: String, password: String): LiveData<Resource<UserEntity>> {
        return authRepository.authenticateTeacher(username, password)
    }

    private fun queryAuthUUID(uuid: String): LiveData<AuthResource<UserEntity>> {
        return LiveDataReactiveStreams.fromPublisher(authRepository.getAuthenticatedUserFromDb(uuid)
            .map {
                if (it.isEmpty()) return@map AuthResource.error("user not found", null)
                return@map AuthResource.authenticated(it[0])
            })
    }

    fun authenticateWithUUID(uuid: String) {
        sessionManager.authenticateWith(queryAuthUUID(uuid))
    }

    fun authCachedUserData(): LiveData<AuthResource<UserEntity>> = sessionManager.getCachedUser()

}