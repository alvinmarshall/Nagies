package com.wNagiesEducationalCenterj_9905.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wNagiesEducationalCenterj_9905.data.db.DAO.MessageDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.StudentDao
import com.wNagiesEducationalCenterj_9905.data.db.DAO.UserDao
import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.StudentProfileEntity
import com.wNagiesEducationalCenterj_9905.data.db.Entities.UserEntity
import com.wNagiesEducationalCenterj_9905.data.db.converter.DateTypeConverter

@Database(
    entities = [
        UserEntity::class,
        MessageEntity::class,
        StudentProfileEntity::class
    ], version = 1, exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun studentDao(): StudentDao
}