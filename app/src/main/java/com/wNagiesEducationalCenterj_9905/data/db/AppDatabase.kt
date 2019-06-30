package com.wNagiesEducationalCenterj_9905.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wNagiesEducationalCenterj_9905.data.db.DAO.UserDao
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity

@Database(entities = [UserEntity::class],version = 1,exportSchema = false)
abstract class AppDatabase :RoomDatabase(){
    abstract fun userDao(): UserDao
//    abstract fun messageDao():MessageDao
}