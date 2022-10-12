package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.Repository
import retrofit2.HttpException

class RefreshAsteroidsDaily(appContext: Context, params: WorkerParameters):
 CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database= getDatabase(applicationContext)
        val repository=Repository(database)
        return try {

            repository.refreshAsteroids()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }
    companion object{
       const val WORK_NAME="RefreshAsteroidsDaily"
    }

}