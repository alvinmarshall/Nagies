package com.wNagiesEducationalCenterj_9905.di.modules

import android.app.Application
import androidx.room.Room
import com.wNagiesEducationalCenterj_9905.common.DATABASE_NAME
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideRoomDb(application: Application): AppDatabase {
        return Room
            .databaseBuilder(application.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao = database.messageDao()

    @Singleton
    @Provides
    fun provideStudentDao(database: AppDatabase): StudentDao = database.studentDao()

    @Singleton
    @Provides
    fun provideComplaintDao(database: AppDatabase): ComplaintDao = database.complaintDao()

    @Singleton
    @Provides
    fun provideAssignmentDao(database: AppDatabase): AssignmentDao = database.assignmentDao()
}