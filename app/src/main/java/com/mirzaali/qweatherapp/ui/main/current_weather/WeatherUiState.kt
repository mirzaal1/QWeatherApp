package com.mirzaali.qweatherapp.ui.main.current_weather


sealed class WeatherUiState<out T> {
    object Idle : WeatherUiState<Nothing>()
    object Loading : WeatherUiState<Nothing>()
    data class Success<T>(val data: T) : WeatherUiState<T>()
    data class Error(val message: String?) : WeatherUiState<Nothing>()
}
