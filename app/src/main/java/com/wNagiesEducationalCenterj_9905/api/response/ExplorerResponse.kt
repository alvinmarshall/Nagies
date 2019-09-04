package com.wNagiesEducationalCenterj_9905.api.response


import com.google.gson.annotations.SerializedName

data class ExplorerResponse(
    val status: Int,
    val count: Int,
    val dataUpload: List<DataUpload>
)

data class DataUpload(
    val id: Int,
    @SerializedName("Students_No")
    val studentsNo: String,
    @SerializedName("Students_Name")
    val studentsName: String,
    @SerializedName("Teachers_Email")
    val teachersEmail: String,
    @SerializedName("Report_File")
    var fileUrl: String?,
    @SerializedName("Report_Date")
    val date: String,
    var format:String?
)