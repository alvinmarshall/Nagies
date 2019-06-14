package com.wNagiesEducationalCenterj_9905

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.vo.AuthResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val cachedUser:MediatorLiveData<AuthResource<UserEntity>> = MediatorLiveData()
    fun authenticateWith(source: LiveData<AuthResource<UserEntity>>){
        cachedUser.value = AuthResource.loading(null)
        cachedUser.addSource(source){
            cachedUser.value = it
            cachedUser.removeSource(source)
        }
    }

    fun getCachedUser():LiveData<AuthResource<UserEntity>> = cachedUser

//    fun logoutUser(){
//        cachedUser.value = AuthResource.logout()
//    }


}