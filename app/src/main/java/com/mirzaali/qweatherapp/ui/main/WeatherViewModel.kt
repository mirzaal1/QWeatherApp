package com.mirzaali.qweatherapp.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirzaali.qweatherapp.data.local.CityPreferenceDataStore
import com.mirzaali.qweatherapp.data.mapper.localize
import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.domain.usecase.GetCachedForecastUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetCitiesUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetWeatherForecastUseCase
import com.mirzaali.qweatherapp.data.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val getWeatherForecast: GetWeatherForecastUseCase,
                                           private val getCachedForecast: GetCachedForecastUseCase,
                                           private val getCities: GetCitiesUseCase,
                                           private val cityPrefs: CityPreferenceDataStore) :
    ViewModel() {

    private val _forecastState = mutableStateOf<WeatherUiState<WeatherForecast>>(WeatherUiState.Idle)
    val forecastState: State<WeatherUiState<WeatherForecast>> = _forecastState

    private val _citiesState = mutableStateOf<WeatherUiState<List<City>>>(WeatherUiState.Idle)
    val citiesState: State<WeatherUiState<List<City>>> = _citiesState

    private val _cities = mutableStateOf<List<City>>(emptyList())
    val cities: List<City> get() = _cities.value

    private var forecastJob: Job? = null
    private var citiesJob: Job? = null

    fun saveSelectedCity(cityId: Int) {
        viewModelScope.launch {
            cityPrefs.saveSelectedCityId(cityId)
        }
    }

    fun reloadDataWithNewLocale(locale: Locale) {
        citiesJob?.cancel()
        citiesJob = viewModelScope.launch {
            getCities().collect { result ->
                _citiesState.value = when (result) {
                    is ResponseResult.Loading -> WeatherUiState.Loading
                    is ResponseResult.Success -> WeatherUiState.Success(result.data.localize(locale))
                    is ResponseResult.Error -> WeatherUiState.Error(result.message)
                }
            }
        }

        forecastJob?.cancel()
        forecastJob = viewModelScope.launch {
            val cityId = cityPrefs.selectedCityIdFlow.firstOrNull()
            if (cityId != null) {
                getCachedForecast(cityId).collect { result ->
                    _forecastState.value = when (result) {
                        is ResponseResult.Loading -> WeatherUiState.Loading
                        is ResponseResult.Success -> WeatherUiState.Success(
                            result.data.localize(
                                locale
                            )
                        )
                        is ResponseResult.Error -> WeatherUiState.Error(result.message)
                    }
                }
            }
        }
    }

    suspend fun hasCitySelected(): Boolean {
        return cityPrefs.selectedCityIdFlow.firstOrNull() != null
    }

    fun loadLastCityForecast() {
        if (forecastState.value is WeatherUiState.Success || forecastJob?.isActive == true) return

        forecastJob = viewModelScope.launch {
            cityPrefs.selectedCityIdFlow.firstOrNull()?.let { cityId ->
                getCachedForecast(cityId).collect { result ->
                    _forecastState.value = when (result) {
                        is ResponseResult.Loading -> WeatherUiState.Loading
                        is ResponseResult.Success -> WeatherUiState.Success(result.data)
                        is ResponseResult.Error -> WeatherUiState.Error(result.message)
                    }
                }
            }
        }
    }


    fun loadCities() {
        if (citiesState.value is WeatherUiState.Success || citiesJob?.isActive == true) return

        citiesJob = viewModelScope.launch {
            getCities().collect { result ->
                _citiesState.value = when (result) {
                    is ResponseResult.Loading -> WeatherUiState.Loading
                    is ResponseResult.Success -> WeatherUiState.Success(result.data)
                    is ResponseResult.Error -> WeatherUiState.Error(result.message)
                }
            }
        }
    }


    fun loadForecast(cityId: Int) {
        forecastJob?.cancel()
        forecastJob = viewModelScope.launch {
            getWeatherForecast(cityId).collect { result ->
                _forecastState.value = when (result) {
                    is ResponseResult.Loading -> WeatherUiState.Loading
                    is ResponseResult.Success -> WeatherUiState.Success(result.data)
                    is ResponseResult.Error -> WeatherUiState.Error(result.message)
                }
            }
        }
    }

}
