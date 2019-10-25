package com.wNagiesEducationalCenterj_9905.di.key

import androidx.work.RxWorker
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out RxWorker>)
