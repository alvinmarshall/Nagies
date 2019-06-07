package com.wNagiesEducationalCenterj_9905.di.modules

import com.wNagiesEducationalCenterj_9905.NavigationActivity
import com.wNagiesEducationalCenterj_9905.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeNavigationActivity():NavigationActivity

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity():AuthActivity
}