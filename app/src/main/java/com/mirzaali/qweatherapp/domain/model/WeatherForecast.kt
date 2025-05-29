package com.mirzaali.qweatherapp.domain.model


data class WeatherForecast(
    val city: CityInfo,
    val current: CurrentWeather,
    val daily: List<DailyWeather>,
    val hourly: List<HourlyWeather>
)

data class CityInfo(
    val id: Int,
    val name: String,
    val nameAr: String,
    val country: String,
    val countryName: String,
    val countryNameAr: String,
    val latitude: Double,
    val longitude: Double,
    val utcOffset: String
)

data class CurrentWeather(
    val temperature: Double,
    val feelsLike: Double,
    val weatherType: String,
    val weatherTypeAr: String,
    val weatherIcon: String,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val visibility: Int,
    val sunrise: Long,
    val sunset: Long,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val clouds: Int,
    val pressure: Int,
    val uvIndex: String,
    val rain: Double,
    val rainUnit: String,
    val temperatureUnit: String,
    val humidityUnit: String,
    val windSpeedUnit: String,
    val visibilityUnit: String,
    val pressureUnit: String,
    val feelsLikeUnit: String,
)

data class DailyWeather(
    val timestamp: Long,
    val temperature: Double,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val temperatureNight: Double,
    val temperatureEve: Double,
    val temperatureMorn: Double,
    val feelsLikeDay: Double,
    val feelsLikeNight: Double,
    val feelsLikeEve: Double,
    val feelsLikeMorn: Double,
    val humidity: Int,
    val pressure: Int,
    val clouds: Int,
    val pop: Double,
    val rain: Double?,
    val windSpeed: Double,
    val windDirection: Int,
    val weatherType: String,
    val weatherTypeAr: String,
    val weatherIcon: String,
    val sunrise: Long,
    val sunset: Long,
    val feelsLikeUnit: String,
)

data class HourlyWeather(
    val date: String,
    val time: String,
    val temperature: Double,
    val humidity: Int,
    val weatherType: String,
    val weatherTypeAr: String,
    val weatherIcon: String?,
    val timestamp: Long,
    val windSpeed: Double,
    val windDirection: Int,
    val windPowerUnit: String,
    val temperatureUnit: String,
)