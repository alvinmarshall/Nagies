package com.wNagiesEducationalCenterj_9905.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wNagiesEducationalCenterj_9905.viewmodel.ViewModelFactory
import com.wNagiesEducationalCenterj_9905.di.key.ViewModelKey
import com.wNagiesEducationalCenterj_9905.ui.auth.viewmodel.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {
    @Binds
    @ViewModelKey(AuthViewModel::class)
    @IntoMap
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory):ViewModelProvider.Factory

}