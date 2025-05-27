package com.mirzaali.qweatherapp.domain.usecase

import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.data.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int): WeatherForecast = repository.getWeatherForecast(cityId)
}