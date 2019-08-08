package com.wNagiesEducationalCenterj_9905.api.response


import com.wNagiesEducationalCenterj_9905.data.db.Entities.AnnouncementEntity

data class AnnouncementResponse(
    val type: String,
    val message: String,
    val count: Int,
    val messages: List<AnnouncementEntity>,
    val status: Int
)