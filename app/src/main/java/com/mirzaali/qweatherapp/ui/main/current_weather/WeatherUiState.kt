package com.mirzaali.qweatherapp.ui.main.current_weather

import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast


sealed class WeatherUiState {

    data object Loading : WeatherUiState()

    data class CitiesLoaded(val cities: List<City>) : WeatherUiState()

    data class ForecastLoaded(val forecast: WeatherForecast) : WeatherUiState()

    data class Error(val message: String?) : WeatherUiState()

    data object Idle : WeatherUiState()
}