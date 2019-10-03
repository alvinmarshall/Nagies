package com.wNagiesEducationalCenterj_9905.api.response

import com.wNagiesEducationalCenterj_9905.vo.IMessageResponseModel

data class TeacherMessageResponse(
    val level: String?,
    override val status: Int,
    override val message: String?,
    override val id: String?
) : IMessageResponseModel