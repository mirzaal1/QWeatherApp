package com.mirzaali.qweatherapp.ui.main.current_weather


/*
sealed class WeatherUiState {

    data object Loading : WeatherUiState()

    data class CitiesLoaded(val cities: List<City>) : WeatherUiState()

    data class ForecastLoaded(val forecast: WeatherForecast) : WeatherUiState()

    data class Error(val message: String?) : WeatherUiState()

    data object Idle : WeatherUiState()
}
*/


sealed class WeatherUiState<out T> {
    object Idle : WeatherUiState<Nothing>()
    object Loading : WeatherUiState<Nothing>()
    data class Success<T>(val data: T) : WeatherUiState<T>()
    data class Error(val message: String?) : WeatherUiState<Nothing>()
}
