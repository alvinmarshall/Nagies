package com.wNagiesEducationalCenterj_9905

import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.jakewharton.threetenabp.AndroidThreeTen
import com.wNagiesEducationalCenterj_9905.common.utils.NotificationUtils
import com.wNagiesEducationalCenterj_9905.di.AppComponent
import com.wNagiesEducationalCenterj_9905.di.DaggerAppComponent
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import org.jetbrains.anko.toast
import timber.log.Timber


class App : DaggerApplication() {

    private val appComponent: AppComponent = DaggerAppComponent.builder().application(this).build()
    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        initTimber()
        AndroidThreeTen.init(this)
        initFCMService()
        NotificationUtils.registerNotificationChannel(applicationContext)
        RxJavaPlugins.setErrorHandler { err -> Timber.i(err) }
        initWorkManager()
    }

    private fun initWorkManager() {
        WorkManager.initialize(this, Configuration.Builder().run {
            setWorkerFactory(appComponent.workerFactory())
                .build()
        })
    }


    override fun applicationInjector() = appComponent

    private fun getGlobalTopic(): String {
        if (BuildConfig.DEBUG) return getString(R.string.fcm_topic_dev_global)
        return getString(R.string.fcm_topic_global)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            toast("developer mode")
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initFCMService() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (!it.isSuccessful) {
                Timber.i("Task Failed")
                return@addOnCompleteListener
            }
            Timber.i("result: ${it.result?.token}")
        }
        FirebaseMessaging.getInstance().subscribeToTopic(getGlobalTopic()).addOnCompleteListener {
            if (!it.isSuccessful) {
                Timber.i("Task Failed")
                return@addOnCompleteListener
            }
            Timber.i("incoming global topic")
        }
    }
}