package com.wNagiesEducationalCenterj_9905.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wNagiesEducationalCenterj_9905.data.db.DAO.*
import com.wNagiesEducationalCenterj_9905.data.db.Entities.*
import com.wNagiesEducationalCenterj_9905.data.db.converter.DateTypeConverter

@Database(
    entities = [
        UserEntity::class,
        MessageEntity::class,
        StudentProfileEntity::class,
        ComplaintEntity::class,
        AssignmentEntity::class,
        ReportEntity::class,
        StudentTeacherEntity::class,
        AnnouncementEntity::class,
        TeacherComplaintEntity::class,
        TeacherProfileEntity::class,
        CircularEntity::class,
        BillingEntity::class,
        ClassStudentEntity::class,
        TimeTableEntity::class
    ], version = 1, exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun studentDao(): StudentDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun reportDao(): ReportDao
    abstract fun teacherDao(): TeacherDao
    abstract fun announcementDao(): AnnouncementDao
}