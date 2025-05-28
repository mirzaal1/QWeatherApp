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
        try {
            val forecast = repository.getWeatherForecast(cityId)
            emit(ResponseResult.Success(forecast))
        } catch (e: Exception) {
            emit(ResponseResult.Error(e.localizedMessage ?: "Unknown error", e))
        }
    }
}