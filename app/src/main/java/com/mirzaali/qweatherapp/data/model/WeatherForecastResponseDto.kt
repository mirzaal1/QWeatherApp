package com.mirzaali.qweatherapp.data.model

data class WeatherForecastResponseDto(
    val Response: WeatherResultWrapper
)

data class WeatherResultWrapper(
    val status: Boolean,
    val result: WeatherResult
)

data class WeatherResult(
    val current_weather: CurrentWeatherDto,
    val daily_weather: List<DailyWeatherDto>,
    val hourly_data: List<HourlyDataDto>? = null,
    val city_id: Int,
    val name: String,
    val name_ar: String,
    val country: String,
    val country_name: String,
    val country_name_ar: String,
    val latitude: Double,
    val longitude: Double,
    val utc_offset: String
)

data class CurrentWeatherDto(
    val time: Long,
    val temperature: Double,
    val temperature_unit: String,
    val weather_type: String,
    val weather_type_ar: String,
    val weather_icon: String,
    val humidity: Int,
    val humidity_unit: String,
    val wind_power: Double,
    val wind_power_unit: String,
    val wind_direction: Int,
    val wind_direction_text: String,
    val wind_direction_text_ar: String,
    val visibility: Int,
    val visibility_unit: String,
    val sunrise: Long,
    val sunset: Long,
    val feels_like: Double,
    val feels_like_unit: String,
    val temperature_min: Double,
    val temperature_max: Double,
    val clouds: Int,
    val pressure: Int,
    val pressure_unit: String,
    val rain: Double,
    val rain_unit: String,
    val uv_index: String,
)

data class DailyWeatherDto(
    val temperature: Double,
    val temperature_min: Double,
    val temperature_max: Double,
    val temperature_night: Double,
    val temperature_eve: Double,
    val temperature_morn: Double,
    val feels_like_day: Double,
    val feels_like_night: Double,
    val feels_like_eve: Double,
    val feels_like_morn: Double,
    val sunrise: Long,
    val sunset: Long,
    val humidity: Int,
    val humidity_min: String?,
    val pressure: Int,
    val clouds: Int,
    val pop: Double,
    val rain: Double,
    val wind_speed: Double,
    val wind_direction: Int,
    val weather_type: String,
    val weather_type_ar: String,
    val weather_icon: String,
    val timestamp: Long,
    val date: String,
    val temperature_unit: String,
    val humidity_unit: String,
    val feels_like_unit: String,
    val pressure_unit: String,
    val rain_unit: String,
    val wind_speed_unit: String
)

data class HourlyDataDto(
    val date: String,
    val day_details: List<HourlyDetailDto>
)

data class HourlyDetailDto(
    val time: String,
    val temperature: Double,
    val humidity: Int,
    val weather_type: String,
    val weather_type_ar: String,
    val weather_icon: String? = null,
    val timestamp: Long,
    val time_hr_qatar: String,
    val wind_power: Double,
    val wind_direction: Int,
    val temperature_unit: String? = null,
    val humidity_unit: String? = null,
    val warning_text: String? = null,
    val warning_text_ar: String? = null,
    val rain: Double? = null,
    val pressure: Int? = null,
    val visibility: Int? = null,
    val visibility_unit: String? = null,
    val pressure_unit: String? = null,
    val rain_unit: String? = null,
    val wind_power_unit: String? = null
)