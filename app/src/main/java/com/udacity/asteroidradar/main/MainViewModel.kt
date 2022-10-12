package com.udacity.asteroidradar.main
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Filter
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _imageOfTheDay=MutableLiveData<PictureOfDay>()
    val imageOfTheDay:LiveData<PictureOfDay>
       get() = _imageOfTheDay

    private val _navigateToSelectedAsteroid=MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid:LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val database= getDatabase(application.applicationContext)
    private val repo=Repository(database)

    fun displayAsteroidDetails(asteroid: Asteroid){
        _navigateToSelectedAsteroid.value=asteroid
    }
    fun displayPropertyDetailsComplete(){
        _navigateToSelectedAsteroid.value=null
    }
    private var _filter = MutableLiveData(Filter.ALL)

    val asteroidList =Transformations.switchMap(_filter) {
        when (it!!) {
            Filter.NEXT_WEEK -> repo.weekAsteroids
            Filter.TODAY -> repo.todayAsteroids
            else -> repo.asteroid_list
        }
    }
    fun onChangeFilter(filter: Filter) {
        _filter.postValue(filter)
    }
    init {
        viewModelScope.launch {
            repo.refreshAsteroids()
            getImageFromNetwork()
        }
    }
   private suspend fun getImageFromNetwork(){
                try {
                    val image=AsteroidApi.retrofitService.getImageOfTheDay(Constants.API_KEY)
                    _imageOfTheDay.postValue(image)
                    Log.e("success","success fetching the image")
                }catch (ex:Exception){
                    Log.e("Fail fetching image",ex.message.toString())
                }
    }
}