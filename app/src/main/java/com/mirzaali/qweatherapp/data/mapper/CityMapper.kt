package com.mirzaali.qweatherapp.data.mapper

import com.mirzaali.qweatherapp.data.model.CityDto
import com.mirzaali.qweatherapp.domain.model.City
import java.util.Locale

fun CityDto.toDomain(): City {
    return City(
        id = city_id,
        name = name,
        arabicName = name_ar,
        countryCode = country,
        countryName = country_name,
        arabicCountryName = country_name_ar,
        coordinates = City.Coordinates(
            latitude = latitude, longitude = longitude
        ),
        timezoneOffset = utc_offset
    )
}

fun List<CityDto>.toDomain(): List<City> = map { it.toDomain() }

fun City.localize(locale: Locale): City {
    val isArabic = locale.language == "ar"
    return this.copy(
        name = if (isArabic) arabicName else name,
        countryName = if (isArabic) arabicCountryName else countryName
    )
}

fun List<City>.localize(locale: Locale): List<City> {
    return map { it.localize(locale) }
}