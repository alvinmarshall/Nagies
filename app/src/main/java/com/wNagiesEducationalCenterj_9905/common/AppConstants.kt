package com.wNagiesEducationalCenterj_9905.common

val LOGIN_ROLE_OPTIONS = arrayListOf("Parent","Teacher")
const val SELECTED_ROLE = "SELECTED_ROLE"
const val INFORDAS_BASE_URL = "https://nagies-portal.herokuapp.com/api/"//"http://192.168.8.101:81/api/ --- http://nagiesportal.000webhostapp.com/api/"
const val DATABASE_NAME = "app_db"
const val LOGIN_PREF = "login_in_status"
const val USER_TOKEN = "user_token"
const val SERVER_URL = "https://nagies-portal.herokuapp.com"//"http://192.168.8.101:81  --- http://nagiesportal.000webhostapp.com"
const val USER_INFO = "user_info"
const val PERMISSION_PREF = "permission_pref"
const val REQUEST_EXTERNAL_STORAGE = 100
enum class DBEntities{
    ASSIGNMENT,REPORT
}
enum class MessageType{
    MESSAGES,FILES
}