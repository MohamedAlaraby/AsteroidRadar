package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshAsteroidsDaily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.stream.DoubleStream.builder

class AsteroidApplication:Application() {
     private val applicationScope= CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
         delayedInit()
    }
    fun delayedInit(){
        applicationScope.launch {
              setUpRecurringWork()
        }
    }

    private fun setUpRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest= PeriodicWorkRequestBuilder<RefreshAsteroidsDaily>(
            1,
            TimeUnit.DAYS,
            ).setConstraints(constraints).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshAsteroidsDaily.WORK_NAME
            //we  will choose keep to just discard the new request work and keep the current one
            ,ExistingPeriodicWorkPolicy.KEEP
            ,repeatingRequest)
    }


}