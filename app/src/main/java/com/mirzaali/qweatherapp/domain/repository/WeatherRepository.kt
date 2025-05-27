package com.mirzaali.qweatherapp.domain.repository

import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import javax.inject.Inject


interface WeatherRepository {
    suspend fun getCities(): List<City>
    suspend fun getWeatherForecast(cityId: Int): WeatherForecast
}

class GetCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): List<City> {
        return repository.getCities()
    }
}

class GetWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int): WeatherForecast {
        return repository.getWeatherForecast(cityId)
    }
}