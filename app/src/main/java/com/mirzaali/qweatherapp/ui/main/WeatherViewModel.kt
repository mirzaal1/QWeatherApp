package com.mirzaali.qweatherapp.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirzaali.qweatherapp.data.local.CityPreferenceDataStore
import com.mirzaali.qweatherapp.domain.usecase.GetCitiesUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetWeatherForecastUseCase
import com.mirzaali.qweatherapp.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherForecast: GetWeatherForecastUseCase,
    private val getCities: GetCitiesUseCase,
    private val cityPrefs: CityPreferenceDataStore
) : ViewModel() {

    private val _uiState = mutableStateOf<WeatherUiState>(WeatherUiState.Idle)
    val uiState: State<WeatherUiState> = _uiState

    fun saveSelectedCity(cityId: Int) {
        viewModelScope.launch {
            cityPrefs.saveSelectedCityId(cityId)
        }
    }

    fun loadLastCityForecast() {
        viewModelScope.launch {
            cityPrefs.selectedCityIdFlow.firstOrNull()?.let { cityId ->
                loadForecast(cityId)
            }
        }
    }

    fun loadCities() {
        viewModelScope.launch {
            getCities().collect { result ->
                when (result) {
                    is ResponseResult.Loading -> _uiState.value = WeatherUiState.Loading
                    is ResponseResult.Success -> _uiState.value = WeatherUiState.CitiesLoaded(result.data)
                    is ResponseResult.Error -> _uiState.value = WeatherUiState.Error(result.message)
                }
            }
        }
    }

    fun loadForecast(cityId: Int) {
        viewModelScope.launch {
            getWeatherForecast(cityId).collect { result ->
                when (result) {
                    is ResponseResult.Loading -> _uiState.value = WeatherUiState.Loading
                    is ResponseResult.Success -> _uiState.value = WeatherUiState.ForecastLoaded(result.data)
                    is ResponseResult.Error -> _uiState.value = WeatherUiState.Error(result.message)
                }
            }
        }
    }

}