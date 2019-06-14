package com.wNagiesEducationalCenterj_9905.di.modules

import android.app.Application
import androidx.room.Room
import com.wNagiesEducationalCenterj_9905.common.DATABASE_NAME
import com.wNagiesEducationalCenterj_9905.data.db.AppDatabase
import com.wNagiesEducationalCenterj_9905.data.db.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideRoomDb(application: Application):AppDatabase{
        return Room
            .databaseBuilder(application.applicationContext,AppDatabase::class.java, DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase):UserDao{
        return database.userDao()
    }
}