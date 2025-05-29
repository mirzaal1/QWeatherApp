package com.mirzaali.qweatherapp.domain.usecase

import com.mirzaali.qweatherapp.data.ResponseResult
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCachedForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(cityId: Int): Flow<ResponseResult<WeatherForecast>> = flow {
        emit(ResponseResult.Loading)

        val cached = repository.getCachedWeatherForecast(cityId)

        if (cached != null) {
            emit(ResponseResult.Success(cached))
        } else {
            emit(ResponseResult.Error("No cached forecast available"))
        }
    }
}
