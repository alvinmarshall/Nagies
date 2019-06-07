package com.wNagiesEducationalCenterj_9905.di

import android.app.Application
import com.wNagiesEducationalCenterj_9905.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class])
interface AppComponent :AndroidInjector<App>{
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application):Builder
        fun build():AppComponent
    }
    override fun inject(app: App)
}