package com.mirzaali.qweatherapp.data.mapper

import com.mirzaali.qweatherapp.data.model.CurrentWeatherDto
import com.mirzaali.qweatherapp.data.model.DailyWeatherDto
import com.mirzaali.qweatherapp.data.model.HourlyDataDto
import com.mirzaali.qweatherapp.data.model.WeatherForecastResponseDto
import com.mirzaali.qweatherapp.data.model.WeatherResult
import com.mirzaali.qweatherapp.domain.model.CityInfo
import com.mirzaali.qweatherapp.domain.model.CurrentWeather
import com.mirzaali.qweatherapp.domain.model.DailyWeather
import com.mirzaali.qweatherapp.domain.model.HourlyWeather
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import java.util.Locale

fun WeatherForecastResponseDto.toDomain(): WeatherForecast {
    val result = Response.result

    return WeatherForecast(
        city = result.toCityInfo(),
        current = result.current_weather.toDomain(),
        daily = result.daily_weather.map { it.toDomain() },
        hourly = result.hourly_data?.flatMap { it.toHourlyList() } ?: emptyList()
    )
}

private fun WeatherResult.toCityInfo(): CityInfo {
    val isArabic = Locale.getDefault().language == "ar"

    return CityInfo(
        id = city_id,
        name = if (isArabic) name_ar else name,
        nameAr = name_ar,
        country = country,
        countryName = if (isArabic) country_name_ar else country_name,
        countryNameAr = country_name_ar,
        latitude = latitude,
        longitude = longitude,
        utcOffset = utc_offset
    )
}


private fun CurrentWeatherDto.toDomain(): CurrentWeather {
    val isArabic = Locale.getDefault().language == "ar"

    return CurrentWeather(
        temperature = temperature,
        feelsLike = feels_like,
        weatherType = if (isArabic) weather_type_ar else weather_type,
        weatherTypeAr = weather_type_ar,
        weatherIcon = weather_icon,
        humidity = humidity,
        windSpeed = wind_power,
        windDirection = wind_direction,
        visibility = visibility,
        sunrise = sunrise,
        sunset = sunset,
        temperatureMin = temperature_min,
        temperatureMax = temperature_max,
        clouds = clouds,
        pressure = pressure,
        uvIndex = uv_index,
        rain = rain,
        rainUnit = rain_unit ,
        temperatureUnit = temperature_unit,
        humidityUnit = humidity_unit,
        windSpeedUnit = wind_power_unit,
        visibilityUnit = visibility_unit,
        pressureUnit = pressure_unit
    )
}

private fun DailyWeatherDto.toDomain(): DailyWeather {
    val isArabic = Locale.getDefault().language == "ar"

    return DailyWeather(
        timestamp = timestamp,
        temperature = temperature,
        temperatureMin = temperature_min,
        temperatureMax = temperature_max,
        temperatureNight = temperature_night,
        temperatureEve = temperature_eve,
        temperatureMorn = temperature_morn,
        feelsLikeDay = feels_like_day,
        feelsLikeNight = feels_like_night,
        feelsLikeEve = feels_like_eve,
        feelsLikeMorn = feels_like_morn,
        humidity = humidity,
        pressure = pressure,
        clouds = clouds,
        pop = pop,
        rain = rain,
        windSpeed = wind_speed,
        windDirection = wind_direction,
        weatherType = if (isArabic) weather_type_ar else weather_type,
        weatherTypeAr = weather_type_ar,
        weatherIcon = weather_icon,
        sunrise = sunrise,
        sunset = sunset
    )
}


private fun HourlyDataDto.toHourlyList(): List<HourlyWeather> {
    val isArabic = Locale.getDefault().language == "ar"
    return day_details.map {
        HourlyWeather(
            date = date,
            time = it.time,
            temperature = it.temperature,
            humidity = it.humidity,
            weatherType = if (isArabic) it.weather_type_ar else it.weather_type,
            weatherTypeAr = it.weather_type_ar,
            weatherIcon = it.weather_icon,
            timestamp = it.timestamp,
            windSpeed = it.wind_power,
            windDirection = it.wind_direction
        )
    }
}

