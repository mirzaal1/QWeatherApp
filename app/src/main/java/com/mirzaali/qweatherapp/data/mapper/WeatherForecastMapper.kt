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
    return CityInfo(
        id = city_id,
        name = name,
        nameAr = name_ar,
        country = country,
        countryName = country_name,
        countryNameAr = country_name_ar,
        latitude = latitude,
        longitude = longitude,
        utcOffset = utc_offset
    )
}


private fun CurrentWeatherDto.toDomain(): CurrentWeather {
    return CurrentWeather(
        temperature = temperature,
        feelsLike = feels_like,
        weatherType = weather_type,
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
        rainUnit = rain_unit,
        temperatureUnit = temperature_unit,
        humidityUnit = humidity_unit,
        windSpeedUnit = wind_power_unit,
        visibilityUnit = visibility_unit,
        pressureUnit = pressure_unit,
        feelsLikeUnit = feels_like_unit
    )
}

private fun DailyWeatherDto.toDomain(): DailyWeather {
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
        weatherType = weather_type,
        weatherTypeAr = weather_type_ar,
        weatherIcon = weather_icon,
        sunrise = sunrise,
        sunset = sunset,
        feelsLikeUnit = feels_like_unit
    )
}


private fun HourlyDataDto.toHourlyList(): List<HourlyWeather> {
    return day_details.map {
        HourlyWeather(
            date = date,
            time = it.time,
            temperature = it.temperature,
            humidity = it.humidity,
            weatherType = it.weather_type,
            weatherTypeAr = it.weather_type_ar,
            weatherIcon = it.weather_icon,
            timestamp = it.timestamp,
            windSpeed = it.wind_power,
            windDirection = it.wind_direction,
            windPowerUnit = it.wind_power_unit,
            temperatureUnit = it.temperature_unit
        )
    }
}

fun WeatherForecast.localize(locale: Locale): WeatherForecast {
    return copy(
        city = city.localized(locale),
        current = current.localized(locale),
        daily = daily.map { it.localized(locale) },
        hourly = hourly.map { it.localized(locale) }
    )
}

fun CityInfo.localized(locale: Locale): CityInfo {
    val isArabic = locale.language == "ar"
    return copy(
        name = if (isArabic) nameAr else name,
        countryName = if (isArabic) countryNameAr else countryName
    )
}

fun CurrentWeather.localized(locale: Locale): CurrentWeather {
    val isArabic = locale.language == "ar"
    return copy(
        weatherType = if (isArabic) weatherTypeAr else weatherType
    )
}

fun DailyWeather.localized(locale: Locale): DailyWeather {
    val isArabic = locale.language == "ar"
    return copy(
        weatherType = if (isArabic) weatherTypeAr else weatherType
    )
}


fun HourlyWeather.localized(locale: Locale): HourlyWeather {
    val isArabic = locale.language == "ar"
    return this.copy(
        weatherType = if (isArabic) weatherTypeAr else weatherType
    )
}



