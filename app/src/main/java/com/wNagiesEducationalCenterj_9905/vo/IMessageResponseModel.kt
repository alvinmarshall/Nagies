package com.wNagiesEducationalCenterj_9905.vo

interface IMessageResponseModel {
    val status:Int
    val type: String
    val message: String?
    val id: String
    val errors: List<String>?
}