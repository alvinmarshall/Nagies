package com.wNagiesEducationalCenterj_9905.di.modules

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.wNagiesEducationalCenterj_9905.common.utils.InputValidationProvider
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        RoomModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
class AppModule {
    @Singleton
    @Provides
    fun provideInputValidator(application: Application): InputValidationProvider {
        return InputValidationProvider(application.baseContext)
    }

    @Singleton
    @Provides
    fun provideSharedPreference(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Singleton
    @Provides
    fun providePreferenceProvider(sharedPreferences: SharedPreferences): PreferenceProvider {
        return PreferenceProvider(sharedPreferences)
    }


}