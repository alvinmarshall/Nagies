package com.wNagiesEducationalCenterj_9905.vo

interface IComplaintModel {
    var id:Int
    var uid:Int
    val studentNo: String
    val studentName: String
    val level: String
    val guardianName: String
    val guardianContact: String
    val teacherName: String
    val message: String
    val date: String
    var token:String?
}