package com.wNagiesEducationalCenterj_9905.di.modules

import com.wNagiesEducationalCenterj_9905.ui.fragment.MessageDetailFragment
import com.wNagiesEducationalCenterj_9905.ui.parent.fragment.DashboardFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeMessageDetailFragement(): MessageDetailFragment
}