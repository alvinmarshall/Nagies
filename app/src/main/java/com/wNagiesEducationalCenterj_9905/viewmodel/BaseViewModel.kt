package com.wNagiesEducationalCenterj_9905.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel :ViewModel(){
    protected val disposable = CompositeDisposable()
    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

}