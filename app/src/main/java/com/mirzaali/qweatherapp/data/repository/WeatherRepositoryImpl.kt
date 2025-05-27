package com.mirzaali.qweatherapp.data.repository

import com.mirzaali.qweatherapp.data.api.WeatherApiService
import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {

    override suspend fun getCities(): List<City> {
        return withContext(dispatcher) {
            apiService.getCities()
                .response.result.cities
                .flatMap { it.value }
                .map { it.toDomain() }
        }
    }

    override suspend fun getWeatherForecast(cityId: Int): WeatherForecast {
        return withContext(dispatcher) {
            apiService.getWeatherForecast(cityId)
                .response.result
                .toDomain()
        }
    }
}