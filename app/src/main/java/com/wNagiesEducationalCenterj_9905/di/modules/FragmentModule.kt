package com.wNagiesEducationalCenterj_9905.di.modules

import com.wNagiesEducationalCenterj_9905.ui.fragment.ChangePasswordFragment
import com.wNagiesEducationalCenterj_9905.ui.fragment.MessageDetailFragment
import com.wNagiesEducationalCenterj_9905.ui.parent.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeDashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun contributeMessageDetailFragement(): MessageDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeStudentProfile(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateMessageFragment(): CreateMessageFragment

    @ContributesAndroidInjector
    abstract fun contributeSendMessageFragment(): SendMessageFragment

    @ContributesAndroidInjector
    abstract fun contributeComplaintDetailFragment(): ComplaintDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeAssignmentPdfFragment(): AssignmentPdfFragment

    @ContributesAndroidInjector
    abstract fun contributeAssignmentJpegFragment(): AssignmentJpegFragment

    @ContributesAndroidInjector
    abstract fun contributeReportPdfFragment(): ReportPdfFragment

    @ContributesAndroidInjector
    abstract fun contributeReportFragment(): ReportFragment

    @ContributesAndroidInjector
    abstract fun contributeChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeClassTeacherFragment(): ClassTeacherFragment
}