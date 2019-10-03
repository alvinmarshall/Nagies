package com.wNagiesEducationalCenterj_9905.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var fetchMessage:MutableLiveData<Boolean> = MutableLiveData()
    var fetchAssignmentJPEG:MutableLiveData<Boolean> = MutableLiveData()
    var fetchReport:MutableLiveData<Boolean> = MutableLiveData()
    var fetchAssignmentPDF:MutableLiveData<Boolean> = MutableLiveData()
    var fetchComplaint:MutableLiveData<Boolean> = MutableLiveData()
    var completedFiles:MutableLiveData<List<Pair<String?,Long?>>> = MutableLiveData()

}