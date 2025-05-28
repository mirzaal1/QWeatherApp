package com.mirzaali.qweatherapp.domain.usecase

import com.mirzaali.qweatherapp.data.repository.WeatherRepository
import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.utils.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<ResponseResult<List<City>>> = flow {
        emit(ResponseResult.Loading)
        try {
            val cities = repository.getCities()
            emit(ResponseResult.Success(cities))
        } catch (e: Exception) {
            emit(ResponseResult.Error(e.localizedMessage ?: "Unknown error", e))
        }
    }

}


