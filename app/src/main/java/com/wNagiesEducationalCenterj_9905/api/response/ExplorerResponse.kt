package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName
import com.wNagiesEducationalCenterj_9905.vo.IFileModel

data class ExplorerResponse(
    val status: Int,
    val count: Int,
    @SerializedName("data")
    val dataUpload: List<DataUpload>
)

data class DataUpload(
    override val studentName: String,
    override val teacherEmail: String,
    override var fileUrl: String?,
    override val date: String,
    override var format: String?,
    override var token: String?,
    override var path: String?,
    override var id: Int,
    override val studentNo: String
) : IFileModel