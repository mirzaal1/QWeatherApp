package com.mirzaali.qweatherapp.domain.repository

import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import kotlinx.coroutines.flow.Flow


interface WeatherRepository {
    fun observeCities(): Flow<List<City>>
    suspend fun fetchAndCacheCities() :List<City>
    fun observeForecast(cityId: Int): Flow<WeatherForecast>
    suspend fun getCachedWeatherForecast(cityId: Int): WeatherForecast?
    suspend fun fetchAndCacheForecast(cityId: Int): WeatherForecast
}
