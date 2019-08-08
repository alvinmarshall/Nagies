package com.wNagiesEducationalCenterj_9905.vo

interface IMessageModel {
    val sender: String
    val level: String
    val content: String
    val read: String?
    var token:String?
    var id:Int
    val date:String
}