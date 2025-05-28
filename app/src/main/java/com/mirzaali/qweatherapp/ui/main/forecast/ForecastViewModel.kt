package com.mirzaali.qweatherapp.ui.main.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirzaali.qweatherapp.data.local.CityPreferenceDataStore
import com.mirzaali.qweatherapp.data.mapper.localize
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.domain.usecase.GetCachedForecastUseCase
import com.mirzaali.qweatherapp.utils.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getCachedForecast: GetCachedForecastUseCase,
    private val cityPrefs: CityPreferenceDataStore
) : ViewModel() {


    private val _forecast = MutableStateFlow<WeatherForecast?>(null)
    val forecast: StateFlow<WeatherForecast?> = _forecast

    init {
        viewModelScope.launch {
            val currentLocale = Locale.getDefault()
            cityPrefs.selectedCityIdFlow.firstOrNull()?.let { cityId ->
                getCachedForecast(cityId).collect { result ->
                    if (result is ResponseResult.Success) {
                        _forecast.value = result.data.localize(currentLocale)
                    }
                }
            }
        }
    }
}
