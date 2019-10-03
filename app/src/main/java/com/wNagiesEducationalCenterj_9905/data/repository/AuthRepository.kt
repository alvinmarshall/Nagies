package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiResponse
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.response.AuthResponse
import com.wNagiesEducationalCenterj_9905.api.request.ChangePasswordRequest
import com.wNagiesEducationalCenterj_9905.api.response.ChangePasswordResponse
import com.wNagiesEducationalCenterj_9905.common.LOGIN_ROLE_OPTIONS
import com.wNagiesEducationalCenterj_9905.common.UserAccount
import com.wNagiesEducationalCenterj_9905.common.utils.ServerPathUtil
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.db.DAO.UserDao
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val preferenceProvider: PreferenceProvider
) {
    fun authenticateUser(role: String, username: String, password: String): LiveData<Resource<UserEntity>> {
        return object : NetworkBoundResource<UserEntity, AuthResponse>(appExecutors) {
            override fun saveCallResult(item: AuthResponse) {
                if (item.Status == 200) {
                    saveUserInfo(username, password, item)
                }
            }

            override fun shouldFetch(data: UserEntity?): Boolean = data == null

            override fun loadFromDb(): LiveData<UserEntity> {
                return userDao.getAuthenticatedUser(username, password)
            }

            override fun createCall(): LiveData<ApiResponse<AuthResponse>> {
                return apiService.getAuthenticatedUser(role, UserEntity(username, password))
            }

        }.asLiveData()
    }

    private fun saveUserInfo(
        username: String,
        password: String,
        item: AuthResponse
    ) {
        val entity = UserEntity(username, password)
        entity.role = item.role
        entity.photo = ServerPathUtil.setCorrectPath(item.image)
        entity.token = item.Token
        entity.uid = item.Id
        entity.name = item.name
        entity.level = item.level
        userDao.insertUser(entity)
        preferenceProvider.setUserLogin(true, entity.token)
        preferenceProvider.setUserBasicInfo(username,entity.name,entity.level,entity.photo)
    }

    fun getAuthenticatedUserFromDb(token: String): Single<UserEntity> {
        return userDao.getAuthenticatedUserWithToken(token)
    }

    fun changeAccountPassword(token: String, changePasswordRequest: ChangePasswordRequest, userAccount: UserAccount)
            : Observable<ChangePasswordResponse> {
        return when (userAccount) {
            UserAccount.PARENT -> apiService.requestParentAccountPasswordChange(token, changePasswordRequest)
            UserAccount.TEACHER -> apiService.requestTeacherAccountPasswordChange(token, changePasswordRequest)
        }
    }

    fun updateAccountPassword(token: String, newPassword: String): Single<Int> {
        return userDao.updateAccountPassword(newPassword, token)
    }


}