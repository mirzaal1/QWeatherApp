package com.mirzaali.qweatherapp.ui.main.current_weather

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirzaali.qweatherapp.data.local.CityPreferenceDataStore
import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.usecase.GetCachedForecastUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetCitiesUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetWeatherForecastUseCase
import com.mirzaali.qweatherapp.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val getWeatherForecast: GetWeatherForecastUseCase,
                                           private val getCachedForecast: GetCachedForecastUseCase,
                                           private val getCities: GetCitiesUseCase,
                                           private val cityPrefs: CityPreferenceDataStore) :
    ViewModel() {

    private val _uiState = mutableStateOf<WeatherUiState>(WeatherUiState.Idle)
    val uiState: State<WeatherUiState> = _uiState

    private val _cities = mutableStateOf<List<City>>(emptyList())
    val cities: List<City> get() = _cities.value

    private var forecastJob: Job? = null
    private var citiesJob: Job? = null

    fun saveSelectedCity(cityId: Int) {
        viewModelScope.launch {
            cityPrefs.saveSelectedCityId(cityId)
        }
    }

    suspend fun hasCitySelected(): Boolean {
        return cityPrefs.selectedCityIdFlow.firstOrNull() != null
    }

    fun loadLastCityForecast() {
        if (_uiState.value is WeatherUiState.ForecastLoaded || forecastJob?.isActive == true) return
        forecastJob = viewModelScope.launch {
            cityPrefs.selectedCityIdFlow.firstOrNull()
                ?.let { cityId ->
                    getCachedForecast(cityId).collect { result ->
                        when (result) {
                            is ResponseResult.Loading -> if (_uiState.value !is WeatherUiState.ForecastLoaded) {
                                _uiState.value = WeatherUiState.Loading
                            }

                            is ResponseResult.Success -> {
                                _uiState.value = WeatherUiState.ForecastLoaded(result.data)
                            }

                            is ResponseResult.Error -> if (_uiState.value !is WeatherUiState.ForecastLoaded) {
                                _uiState.value = WeatherUiState.Error(result.message)
                            }
                        }
                    }
                }
        }
    }

    fun loadCities() {
        if (_uiState.value is WeatherUiState.CitiesLoaded || citiesJob?.isActive == true) return
        citiesJob = viewModelScope.launch {
            getCities().collect { result ->
                when (result) {
                    is ResponseResult.Loading -> if (_uiState.value !is WeatherUiState.CitiesLoaded) {
                        _uiState.value = WeatherUiState.Loading
                    }

                    is ResponseResult.Success -> {
                        _cities.value = result.data
                        _uiState.value = WeatherUiState.CitiesLoaded(result.data)
                    }

                    is ResponseResult.Error -> if (_uiState.value !is WeatherUiState.CitiesLoaded) {
                        _uiState.value = WeatherUiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun loadForecast(cityId: Int) {
        forecastJob?.cancel()
        forecastJob = viewModelScope.launch {
            getWeatherForecast(cityId).collect { result ->
                when (result) {
                    is ResponseResult.Loading -> _uiState.value = WeatherUiState.Loading
                    is ResponseResult.Success -> _uiState.value =
                        WeatherUiState.ForecastLoaded(result.data)

                    is ResponseResult.Error -> _uiState.value = WeatherUiState.Error(result.message)
                }
            }
        }
    }
}
