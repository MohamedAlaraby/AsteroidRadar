package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AsteroidDao{
     @Query("select * from AsteroidEntity ORDER BY closeApproachDate desc")
     fun getAllAsteroids():LiveData<List<AsteroidEntity>>
     @Query("select * from AsteroidEntity where closeApproachDate=:startDate ORDER BY closeApproachDate desc")
     fun getTodayAsteroids(startDate:String):LiveData<List<AsteroidEntity>>
     @Query("select * from AsteroidEntity where closeApproachDate between  :startDate and :endDate order by closeApproachDate desc")
     fun getNextSevenDaysAsteroids(startDate:String ,endDate:String):LiveData<List<AsteroidEntity>>
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(vararg asteroidEntity: AsteroidEntity)

}
@Database(entities =[AsteroidEntity::class] , version = 1, exportSchema = false)
abstract class AsteroidDB: RoomDatabase() {
     abstract val asteroidDao:AsteroidDao
}

private lateinit var INSTANCE:AsteroidDB
fun getDatabase(context:Context):AsteroidDB{
     synchronized(AsteroidDB::class.java){
          if (!::INSTANCE.isInitialized){
               INSTANCE=Room.databaseBuilder(context.applicationContext
                    ,AsteroidDB::class.java,"asteroids").build()
          }
     }
     return INSTANCE
}



