package com.wNagiesEducationalCenterj_9905.api.response

import com.wNagiesEducationalCenterj_9905.vo.IMessageResponseModel


data class ParentComplaintResponse(
    override val status: Int,
    override val type: String,
    override val message: String?,
    override val id: String,
    override val errors: List<String>?
) : IMessageResponseModel