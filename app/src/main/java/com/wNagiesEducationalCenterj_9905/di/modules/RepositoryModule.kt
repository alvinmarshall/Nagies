package com.wNagiesEducationalCenterj_9905.di.modules

import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.MessageDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.StudentDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.UserDao
import com.wNagiesEducationalCenterj_9905.data.repository.AuthRepository
import com.wNagiesEducationalCenterj_9905.data.repository.StudentRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(
        apiService: ApiService,
        appExecutors: AppExecutors,
        userDao: UserDao,
        preferenceProvider: PreferenceProvider
    ): AuthRepository {
        return AuthRepository(apiService, appExecutors, userDao, preferenceProvider)
    }

    @Singleton
    @Provides
    fun provideStudentRepository(
        appExecutors: AppExecutors,
        apiService: ApiService,
        messageDao: MessageDao,
        database: AppDatabase,
        studentDao: StudentDao
    ): StudentRepository {
        return StudentRepository(appExecutors, apiService, messageDao, database, studentDao)
    }

}