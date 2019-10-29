package com.wNagiesEducationalCenterj_9905.di.modules.workmanager

import androidx.work.RxWorker
import com.wNagiesEducationalCenterj_9905.di.key.WorkerKey
import com.wNagiesEducationalCenterj_9905.jobs.UploadFilesWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(UploadFilesWorker::class)
    abstract fun bindUploadFilesWorker(uploadFilesWorker: UploadFilesWorker): RxWorker
}