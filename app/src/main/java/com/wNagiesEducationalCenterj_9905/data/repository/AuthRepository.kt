package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiResponse
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.AuthResponse
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.db.UserDao
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Flowable
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val preferenceProvider: PreferenceProvider
) {
    fun authenticateParent(username: String, password: String): LiveData<Resource<UserEntity>> {
        return object : NetworkBoundResource<UserEntity, AuthResponse>(appExecutors) {
            override fun saveCallResult(item: AuthResponse) {
                if (item.Status == 1) {
                    userDao.insertUser(UserEntity(item.UUID,item.Username, item.Password))
                    preferenceProvider.setUserLogin(true,item.UUID)
                }
            }

            override fun shouldFetch(data: UserEntity?): Boolean = data == null

            override fun loadFromDb(): LiveData<UserEntity> {
                return userDao.getAuthenticatedUser(username, password)
            }

            override fun createCall(): LiveData<ApiResponse<AuthResponse>> {
                return apiService.getAuthenticatedParent(username, password)
            }
        }.asLiveData()
    }

    fun authenticateTeacher(username: String, password: String): LiveData<Resource<UserEntity>> {
        return object : NetworkBoundResource<UserEntity, AuthResponse>(appExecutors) {
            override fun saveCallResult(item: AuthResponse) {
                if (item.Status == 1) {
                    userDao.insertUser(UserEntity(item.UUID,item.Username, item.Password))
                    preferenceProvider.setUserLogin(true,item.UUID)
                }
            }

            override fun shouldFetch(data: UserEntity?): Boolean = data == null

            override fun loadFromDb(): LiveData<UserEntity> {
                return userDao.getAuthenticatedUser(username, password)
            }

            override fun createCall(): LiveData<ApiResponse<AuthResponse>> {
                return apiService.getAuthenticatedTeacher(username, password)
            }
        }.asLiveData()
    }

    fun getAuthenticatedUserFromDb(uuid:String):Flowable<List<UserEntity>>{
        return userDao.getAuthenticatedUserWithUUID(uuid)
    }

}