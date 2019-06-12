package com.wNagiesEducationalCenterj_9905.di.modules

import com.wNagiesEducationalCenterj_9905.ui.parent.ParentNavigationActivity
import com.wNagiesEducationalCenterj_9905.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeNavigationActivity(): ParentNavigationActivity

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity():AuthActivity
}