package com.mirzaali.qweatherapp.data.api

import com.mirzaali.qweatherapp.data.model.CitiesResponseDto
import com.mirzaali.qweatherapp.data.model.WeatherForecastResponseDto
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApiService {

    @POST("cities")
    suspend fun getCities(): CitiesResponseDto

    @POST("current_weather/city")
    suspend fun getForecastByCityId(
        @Query("city_id") cityId: Int
    ): WeatherForecastResponseDto

}