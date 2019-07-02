package com.wNagiesEducationalCenterj_9905.di.modules

import android.app.Application
import androidx.room.Room
import com.wNagiesEducationalCenterj_9905.common.DATABASE_NAME
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.MessageDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.UserDao
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
}