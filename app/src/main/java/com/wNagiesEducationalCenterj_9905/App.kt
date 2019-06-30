package com.wNagiesEducationalCenterj_9905

import com.wNagiesEducationalCenterj_9905.di.AppComponent
import com.wNagiesEducationalCenterj_9905.di.DaggerAppComponent
import dagger.android.DaggerApplication
import timber.log.Timber


class App : DaggerApplication() {
    private val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

    }

    override fun applicationInjector() = appComponent

}