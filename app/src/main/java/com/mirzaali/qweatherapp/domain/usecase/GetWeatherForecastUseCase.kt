package com.mirzaali.qweatherapp.domain.usecase

import com.mirzaali.qweatherapp.data.repository.WeatherRepository
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.utils.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class GetWeatherForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(cityId: Int): Flow<ResponseResult<WeatherForecast>> = flow {
        emit(ResponseResult.Loading)
        val cached = repository.getCachedWeatherForecast(cityId)
        if (cached != null) {
            emit(ResponseResult.Success(cached))
        }
        runCatching {
            repository.fetchAndCacheForecast(cityId)
        }.onSuccess {
            val updated = repository.getCachedWeatherForecast(cityId)
            if (updated != null) {
                emit(ResponseResult.Success(updated))
            }
        }.onFailure { e ->
            if (cached == null) {
                emit(ResponseResult.Error(e.localizedMessage ?: "Unknown error", e))
            }
        }
    }
}
