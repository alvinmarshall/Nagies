package com.wNagiesEducationalCenterj_9905.api.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("old_password")
    var oldPassword:String?,
    @SerializedName("new_password")
    var newPassword:String?,
    @SerializedName("confirm_password")
    var confirmPassword:String?
)