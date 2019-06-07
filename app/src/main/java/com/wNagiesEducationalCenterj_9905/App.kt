package com.wNagiesEducationalCenterj_9905

import com.wNagiesEducationalCenterj_9905.di.AppComponent
import com.wNagiesEducationalCenterj_9905.di.DaggerAppComponent
import dagger.android.DaggerApplication


class App : DaggerApplication() {
    private val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun applicationInjector() = appComponent

}