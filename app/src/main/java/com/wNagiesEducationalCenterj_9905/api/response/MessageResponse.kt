package com.wNagiesEducationalCenterj_9905.api.response

import com.wNagiesEducationalCenterj_9905.data.db.Entities.MessageEntity


data class MessageResponse(
    val type: String,
    val status: Int,
    val message: String,
    val count: Int,
    val messages: List<MessageEntity>
)