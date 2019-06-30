package com.wNagiesEducationalCenterj_9905.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wNagiesEducationalCenterj_9905.common.utils.PreferenceProvider
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : DaggerFragment(),CoroutineScope{
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preferenceProvider: PreferenceProvider
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}