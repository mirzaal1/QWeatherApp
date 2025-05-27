package com.mirzaali.qweatherapp.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirzaali.qweatherapp.domain.usecase.GetCitiesUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetWeatherForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherForecast: GetWeatherForecastUseCase,
    private val getCities: GetCitiesUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf<WeatherUiState>(WeatherUiState.Idle)
    val uiState: State<WeatherUiState> = _uiState

    fun loadCities() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val cities = getCities()
                _uiState.value = WeatherUiState.CitiesLoaded(cities)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message)
            }
        }
    }

    fun loadForecast(cityId: Int) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val forecast = getWeatherForecast(cityId)
                _uiState.value = WeatherUiState.ForecastLoaded(forecast)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message)
            }
        }
    }
}




