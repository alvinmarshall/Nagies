package com.wNagiesEducationalCenterj_9905.api.request

import com.wNagiesEducationalCenterj_9905.vo.IMessageRequestModel

data class TeacherMessageRequest(
    override val content: String
) : IMessageRequestModel{
    var title: String = "message from class teacher"
    var condition: String = "'parent' in topics "
}