package com.mirzaali.qweatherapp.data.mapper

import com.mirzaali.qweatherapp.data.model.CurrentWeatherDto
import com.mirzaali.qweatherapp.data.model.DailyWeatherDto
import com.mirzaali.qweatherapp.data.model.HourlyDetailDto
import com.mirzaali.qweatherapp.data.model.WeatherForecastResponseDto
import com.mirzaali.qweatherapp.data.model.WeatherResult
import com.mirzaali.qweatherapp.domain.model.CityInfo
import com.mirzaali.qweatherapp.domain.model.CurrentWeather
import com.mirzaali.qweatherapp.domain.model.DailyWeather
import com.mirzaali.qweatherapp.domain.model.HourlyWeather
import com.mirzaali.qweatherapp.domain.model.WeatherForecast


fun WeatherForecastResponseDto.toDomain(): WeatherForecast {
    val result = Response.result

    return WeatherForecast(
        city = result.toCityInfo(),
        current = result.current_weather.toCurrentWeather(),
        daily = result.daily_weather.map { it.toDailyWeather() },
        hourly = result.hourly_data.flatMap { day ->
            day.day_details.map {
                it.toHourlyWeather(day.date)
            }
        }
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

private fun CurrentWeatherDto.toCurrentWeather(): CurrentWeather {
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
        uvIndex = uv_index
    )
}

private fun DailyWeatherDto.toDailyWeather(): DailyWeather {
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
        sunset = sunset
    )
}

private fun HourlyDetailDto.toHourlyWeather(date: String): HourlyWeather {
    return HourlyWeather(
        date = date,
        time = time,
        temperature = temperature,
        humidity = humidity,
        weatherType = weather_type,
        weatherTypeAr = weather_type_ar,
        weatherIcon = weather_icon,
        timestamp = timestamp,
        windSpeed = wind_power,
        windDirection = wind_direction
    )
}
