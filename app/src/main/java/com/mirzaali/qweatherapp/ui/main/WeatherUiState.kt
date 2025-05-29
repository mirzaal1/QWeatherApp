package com.mirzaali.qweatherapp.ui.main


sealed class WeatherUiState<out T> {
    data object Idle : WeatherUiState<Nothing>()
    data object Loading : WeatherUiState<Nothing>()
    data class Success<T>(val data: T) : WeatherUiState<T>()
    data class Error(val message: String?) : WeatherUiState<Nothing>()
}
