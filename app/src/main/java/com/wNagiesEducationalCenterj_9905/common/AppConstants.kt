package com.wNagiesEducationalCenterj_9905.common

val LOGIN_ROLE_OPTIONS = arrayListOf("Parent", "Teacher")
const val SELECTED_ROLE = "SELECTED_ROLE"
const val INFORDAS_BASE_URL =
    "https://infodasgh.com/api/"//"http://192.168.8.101:81/api/ --- http://nagiesportal.000webhostapp.com/api/"
const val DATABASE_NAME = "app_db"
const val LOGIN_PREF = "login_in_status"
const val USER_TOKEN = "user_token"
const val SERVER_URL =
    "https://infodasgh.com/api/download?path="//"http://192.168.8.101:81  --- http://nagiesportal.000webhostapp.com" ===https://nagies-portal.herokuapp.com
const val USER_INFO = "user_info"
const val PERMISSION_PREF = "permission_pref"
const val REQUEST_EXTERNAL_STORAGE = 100

enum class DBEntities {
    ASSIGNMENT, REPORT, CIRCULAR,BILLING
}

enum class MessageType {
    MESSAGES, FILES, TEACHERS
}

enum class ClassTeacherAction {
    CALL, MESSAGE
}

enum class UserAccount {
    PARENT, TEACHER
}

enum class ComplaintAction {
    DETAILS, CALL, MESSAGE
}

const val MESSAGE_RECEIVE_EXTRA = "message_extra"
const val MESSAGE_BROADCAST_ACTION = "com.wNagiesEducationalCenterj_9905.onMessageReceived"
const val NOTIFICATION_EXTRA_MESSAGE = "notification_message_extra"
const val NOTIFICATION_EXTRA_REPORT = "notification_report_extra"
const val NOTIFICATION_EXTRA_ASSIGNMENT = "notification_assignment_extra"
const val NOTIFICATION_EXTRA_COMPLAINT = "notification_complaint_extra"

enum class FetchType {
    ASSIGNMENT_PDF, ASSIGNMENT_IMAGE, REPORT_PDF, REPORT_IMAGE, MESSAGE, ANNOUNCEMENT, COMPLAINT, CLASS_TEACHER, CIRCULAR,BILLING,CLASS_STUDENT
}

const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val IMAGE_FORMAT = "image"
const val PDF_FORMAT = "pdf"

enum class CircularAction {
    VIEW, DOWNLOAD
}

enum class ViewFilesAction {
    VIEW, DOWNLOAD, DELETE
}

enum class FileUploadFormat{
    PDF,IMAGE
}

const val FILE_CHOOSER_RESULT = 22
enum class UploadFileType{
    NORMAL,REPORT
}