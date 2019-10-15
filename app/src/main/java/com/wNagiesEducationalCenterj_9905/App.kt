package com.wNagiesEducationalCenterj_9905

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
        if (BuildConfig.DEBUG) {
            toast("developer mode")
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)

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
        NotificationUtils.registerNotificationChannel(applicationContext)

        RxJavaPlugins.setErrorHandler { err -> Timber.i(err) }
    }

    override fun applicationInjector() = appComponent

    private fun getGlobalTopic():String{
        if (BuildConfig.DEBUG) return getString(R.string.fcm_topic_dev_global)
        return getString(R.string.fcm_topic_global)
    }
}