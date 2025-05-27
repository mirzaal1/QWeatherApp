package com.mirzaali.qweatherapp.domain.usecase

import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.data.repository.WeatherRepository
import javax.inject.Inject


class GetCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): List<City> = repository.getCities()
}
