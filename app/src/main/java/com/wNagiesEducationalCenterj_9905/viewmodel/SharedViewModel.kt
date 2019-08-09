package com.wNagiesEducationalCenterj_9905.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var fetchMessage:MutableLiveData<Boolean> = MutableLiveData()
    var fetchAnnouncement:MutableLiveData<Boolean> = MutableLiveData()
    var fetchReport:MutableLiveData<Boolean> = MutableLiveData()
}