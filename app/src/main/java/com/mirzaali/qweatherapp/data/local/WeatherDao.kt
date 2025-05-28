package com.mirzaali.qweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("SELECT * FROM cities")
    fun observeCities(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: ForecastEntity)

    @Query("SELECT * FROM forecast WHERE cityId = :cityId")
    fun observeForecast(cityId: Int): Flow<ForecastEntity?>

    @Query("SELECT * FROM forecast WHERE cityId = :cityId")
    suspend fun getForecast(cityId: Int): ForecastEntity?

}
