package com.wNagiesEducationalCenterj_9905.data.repository

import androidx.lifecycle.LiveData
import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiResponse
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.api.response.AuthResponse
import com.wNagiesEducationalCenterj_9905.api.request.ChangePasswordRequest
import com.wNagiesEducationalCenterj_9905.api.response.ChangePasswordResponse
import com.wNagiesEducationalCenterj_9905.common.UserAccount
import com.wNagiesEducationalCenterj_9905.common.utils.ServerPathUtil
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.db.DAO.UserDao
import com.wNagiesEducationalCenterj_9905.vo.Resource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
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
                if (item.Status == 200) {
                    userDao.insertUserToken(
                        UserEntity(
                            item.Id,
                            username,
                            password,
                            "Bearer " + item.Token,
                            ServerPathUtil.setCorrectPath(item.image)
                        )
                    )
                    preferenceProvider.setUserLogin(true, "Bearer " + item.Token)
                }
            }

            override fun shouldFetch(data: UserEntity?): Boolean = data == null

            override fun loadFromDb(): LiveData<UserEntity> {
                return userDao.getAuthenticatedUser(username, password)
            }

            override fun createCall(): LiveData<ApiResponse<AuthResponse>> {
                return apiService.getAuthenticatedParent(
                    UserEntity(null, username, password, "", null)
                )
            }
        }.asLiveData()
    }

    fun authenticateTeacher(username: String, password: String): LiveData<Resource<UserEntity>> {
        return object : NetworkBoundResource<UserEntity, AuthResponse>(appExecutors) {
            override fun saveCallResult(item: AuthResponse) {
                if (item.Status == 200) {
                    userDao.insertUserToken(
                        UserEntity(
                            item.Id,
                            username,
                            password,
                            "Bearer " + item.Token,
                            ServerPathUtil.setCorrectPath(item.image)
                        )
                    )
                    preferenceProvider.setUserLogin(true, "Bearer " + item.Token)
                }
            }

            override fun shouldFetch(data: UserEntity?): Boolean = data == null

            override fun loadFromDb(): LiveData<UserEntity> {
                return userDao.getAuthenticatedUser(username, password)
            }

            override fun createCall(): LiveData<ApiResponse<AuthResponse>> {
                return apiService.getAuthenticatedTeacher(
                    UserEntity(null, username, password, "", null)
                )
            }
        }.asLiveData()
    }

    fun getAuthenticatedUserFromDb(token: String): Flowable<List<UserEntity>> {
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