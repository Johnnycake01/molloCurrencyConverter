package com.mollo.myapplication.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mollo.myapplication.services.GetCurrentTime

class ConverterWorkManagerUpdateTime (appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    private var getCurrentTime: GetCurrentTime = appContext as GetCurrentTime
    override fun doWork(): Result {
        //interface that trigger work to get current time
        getCurrentTime.getCurrentTime()
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}