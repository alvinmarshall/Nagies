package com.wNagiesEducationalCenterj_9905.vo

data class SessionData(
    val name:String?,
    val level:String?,
    val imageUrl:String?

){
    var token:String?=null
    var username:String?=null
    var userRole:String?=null
    var loginStatus:Boolean=false
}