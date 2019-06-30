package com.wNagiesEducationalCenterj_9905.di.modules

import com.wNagiesEducationalCenterj_9905.SplashActivity
import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.auth.AuthActivity
import com.wNagiesEducationalCenterj_9905.ui.teacher.TeacherNavigationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [FragmentModule::class])
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeAuthActivity():AuthActivity
    @ContributesAndroidInjector
    abstract fun contributeSplashActivity():SplashActivity
    @ContributesAndroidInjector
    abstract fun contributeParentActivity():ParentNavigationActivity
    @ContributesAndroidInjector
    abstract fun contributeTeacherActivity():TeacherNavigationActivity
}