package com.udacity.asteroidradar.repository

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDB
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.utils.endDateFormat
import com.udacity.asteroidradar.utils.startDateFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//the repository is responsible for fetching the data from network and storing them in the database
class Repository(private val database: AsteroidDB) {

    val asteroid_list: LiveData<List<Asteroid>> =
        map(database.asteroidDao.getAllAsteroids()){
            it.asDomainModel()
        }
    @RequiresApi(Build.VERSION_CODES.O)
    val todayAsteroids: LiveData<List<Asteroid>> =
        map(database.asteroidDao.getTodayAsteroids(startDateFormat())) {
            it.asDomainModel()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    val weekAsteroids: LiveData<List<Asteroid>> = map(
            database.asteroidDao.getNextSevenDaysAsteroids(
                startDateFormat(),
                endDateFormat()
            )) {
        it.asDomainModel()
    }
    suspend fun refreshAsteroids(){
         withContext(Dispatchers.IO){
             try{
                 val responseAsString=AsteroidApi.retrofitService.getAsteroids(Constants.API_KEY)
                 val resultList= parseAsteroidsJsonResult(JSONObject(responseAsString))
                 database.asteroidDao.insertAll(*resultList.asDatabaseModel())
             }catch (err: Exception) {
                 Log.e("Failed:",err.message.toString())
             }
     }
}


}