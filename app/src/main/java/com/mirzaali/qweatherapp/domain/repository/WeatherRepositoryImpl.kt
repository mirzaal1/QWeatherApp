package com.mirzaali.qweatherapp.domain.repository

import com.google.gson.Gson
import com.mirzaali.qweatherapp.data.api.WeatherApiService
import com.mirzaali.qweatherapp.data.local.ForecastEntity
import com.mirzaali.qweatherapp.data.local.WeatherDatabase
import com.mirzaali.qweatherapp.data.mapper.toDomain
import com.mirzaali.qweatherapp.data.mapper.toEntity
import com.mirzaali.qweatherapp.data.repository.WeatherRepository
import com.mirzaali.qweatherapp.domain.model.City
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.util.Locale
import javax.inject.Inject


class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
    private val db: WeatherDatabase,
    private val gson: Gson
) : WeatherRepository {

    override fun observeCities(): Flow<List<City>> {
        return db.weatherDao().observeCities().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun fetchAndCacheCities(): List<City> {
        val response = api.getCities()
        val all = response.Response.result.cities.values.flatten()
            .map { it.toDomain() }
        db.weatherDao()
            .insertCities(all.map { it.toEntity() })
        return all
    }

  /*  override fun observeForecast(cityId: Int): Flow<WeatherForecast> {
        return db.weatherDao().observeForecast(cityId).mapNotNull { entity ->
            entity?.forecastJson?.let { gson.fromJson(it, WeatherForecast::class.java) }
        }
    }
*/

    override fun observeForecast(cityId: Int): Flow<WeatherForecast> {
        val isArabic = Locale.getDefault().language == "ar"

        return db.weatherDao().observeForecast(cityId).mapNotNull { entity ->
            entity?.forecastJson?.let { json ->
                val rawForecast = gson.fromJson(json, WeatherForecast::class.java)

                rawForecast.copy(
                    city = rawForecast.city.copy(
                        name = if (isArabic) rawForecast.city.nameAr else rawForecast.city.name,
                        countryName = if (isArabic) rawForecast.city.countryNameAr else rawForecast.city.countryName
                    ),
                    current = rawForecast.current.copy(
                        weatherType = if (isArabic) rawForecast.current.weatherTypeAr else rawForecast.current.weatherType
                    ),
                    daily = rawForecast.daily.map { it.copy(
                        weatherType = if (isArabic) it.weatherTypeAr else it.weatherType
                    ) },
                    hourly = rawForecast.hourly.map { it.copy(
                        weatherType = if (isArabic) it.weatherTypeAr else it.weatherType
                    ) }
                )
            }
        }
    }

    override suspend fun getCachedWeatherForecast(cityId: Int): WeatherForecast? {
      /*  return db.weatherDao().getForecast(cityId)?.forecastJson?.let {
            gson.fromJson(it, WeatherForecast::class.java)
        }*/
        return db.weatherDao().getForecast(cityId)?.forecastJson?.let { json ->
            val rawForecast = gson.fromJson(json, WeatherForecast::class.java)

            // Apply localization manually (like in mapper)
            val isArabic = Locale.getDefault().language == "ar"

            rawForecast.copy(
                city = rawForecast.city.copy(
                    name = if (isArabic) rawForecast.city.nameAr else rawForecast.city.name,
                    countryName = if (isArabic) rawForecast.city.countryNameAr else rawForecast.city.countryName
                ),
                current = rawForecast.current.copy(
                    weatherType = if (isArabic) rawForecast.current.weatherTypeAr else rawForecast.current.weatherType
                ),
                daily = rawForecast.daily.map { it.copy(
                    weatherType = if (isArabic) it.weatherTypeAr else it.weatherType
                ) },
                hourly = rawForecast.hourly.map { it.copy(
                    weatherType = if (isArabic) it.weatherTypeAr else it.weatherType
                ) }
            )
        }
    }

    override suspend fun fetchAndCacheForecast(cityId: Int): WeatherForecast {
        val result = api.getForecastByCityId(cityId)
        val domain = result.toDomain()
        val json = gson.toJson(domain)
        db.weatherDao().insertForecast(ForecastEntity(cityId, json, System.currentTimeMillis()))
        return domain
    }
}