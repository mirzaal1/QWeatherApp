package com.mirzaali.qweatherapp.data.repository

import com.mirzaali.qweatherapp.data.api.WeatherApiService
import com.mirzaali.qweatherapp.data.mapper.toDomain
import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.domain.repository.WeatherRepository
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService
) : WeatherRepository {

    override suspend fun getCities(): List<City> {
        val response = api.getCities()
        val cityMap = response.Response.result.cities

        return cityMap.values.flatten().map { it.toDomain() }
    }

    override suspend fun getWeatherForecast(cityId: Int): WeatherForecast {
        val response = api.getForecastByCityId(cityId)
        return response.toDomain()
    }
}