package com.wNagiesEducationalCenterj_9905.di.modules

import com.wNagiesEducationalCenterj_9905.AppExecutors
import com.wNagiesEducationalCenterj_9905.api.ApiService
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.*
import com.wNagiesEducationalCenterj_9905.data.repository.AuthRepository
import com.wNagiesEducationalCenterj_9905.data.repository.StudentRepository
import com.wNagiesEducationalCenterj_9905.data.repository.TeacherRepository
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
        studentDao: StudentDao,
        complaintDao: ComplaintDao,
        assignmentDao: AssignmentDao,
        reportDao: ReportDao
    ): StudentRepository {
        return StudentRepository(
            appExecutors, apiService, messageDao, database, studentDao, complaintDao, assignmentDao, reportDao
        )
    }

    @Singleton
    @Provides
    fun provideTeacherRepository(
        appExecutors: AppExecutors,
        apiService: ApiService,
        database: AppDatabase,
        teacherDao: TeacherDao,
        announcementDao: AnnouncementDao,
        complaintDao: ComplaintDao,
        messageDao: MessageDao
    ): TeacherRepository {
        return TeacherRepository(appExecutors, apiService, teacherDao, database,announcementDao,complaintDao,messageDao)
    }

}