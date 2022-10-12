package com.udacity.asteroidradar.api
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.domain.PictureOfDay

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi=Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
private val httpClient = OkHttpClient.Builder()
        .addInterceptor( HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()
private val retrofit= Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())

        .baseUrl(Constants.BASE_URL)
        .client(httpClient)
        .build()
interface AsteroidApiService{
      @GET("neo/rest/v1/feed")
      suspend  fun getAsteroids(@Query("api_key") api_key :String):String

      @GET("planetary/apod")
      suspend  fun getImageOfTheDay(@Query("api_key") api_key:String): PictureOfDay
}
//object to make a single object even if there is multiple threads using the object
object AsteroidApi{
        val retrofitService:AsteroidApiService by lazy{
                retrofit.create(AsteroidApiService::class.java)
        }
}