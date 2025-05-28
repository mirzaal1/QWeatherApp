package com.mirzaali.qweatherapp.domain.usecase

import com.mirzaali.qweatherapp.data.repository.WeatherRepository
import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.utils.ResponseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<ResponseResult<List<City>>> = flow {
        emit(ResponseResult.Loading)

        val cached = repository.observeCities().firstOrNull().orEmpty()
        if (cached.isNotEmpty()) {
            emit(ResponseResult.Success(cached))
        }

        runCatching {
            repository.fetchAndCacheCities()
        }.onSuccess {
            val updated = repository.observeCities().firstOrNull().orEmpty()
            emit(ResponseResult.Success(updated))
        }.onFailure { e ->
            if (cached.isEmpty()) {
                emit(ResponseResult.Error(e.localizedMessage ?: "Unknown error", e))
            }
        }
    }
}
