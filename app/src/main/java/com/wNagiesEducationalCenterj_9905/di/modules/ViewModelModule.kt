package com.wNagiesEducationalCenterj_9905.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wNagiesEducationalCenterj_9905.viewmodel.ViewModelFactory
import com.wNagiesEducationalCenterj_9905.di.key.ViewModelKey
import com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel.AuthViewModel
import com.wNagiesEducationalCenterj_9905.ui.parent.viewmodel.StudentViewModel
import com.wNagiesEducationalCenterj_9905.ui.teacher.viewmodel.TeacherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {
    @Binds
    @ViewModelKey(AuthViewModel::class)
    @IntoMap
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StudentViewModel::class)
    abstract fun bindStudentViewModel(studentViewModel: StudentViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TeacherViewModel::class)
    abstract fun bindsTeacherViewModel(teacherViewModel: TeacherViewModel): ViewModel

}