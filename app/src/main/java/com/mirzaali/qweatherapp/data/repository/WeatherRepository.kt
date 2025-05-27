package com.mirzaali.qweatherapp.data.repository

import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast


interface WeatherRepository {
    suspend fun getCities(): List<City>
    suspend fun getWeatherForecast(cityId: Int): WeatherForecast
}
