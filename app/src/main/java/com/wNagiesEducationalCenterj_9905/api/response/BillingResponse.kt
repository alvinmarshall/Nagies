package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.data.db.Entities.BillingEntity

data class BillingResponse(
    val type: String,
    val message: String,
    val count: Int,
    @SerializedName("data")
    val billing: List<BillingEntity>,
    val status: Int
)