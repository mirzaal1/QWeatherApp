package com.mirzaali.qweatherapp.data.mapper

import com.mirzaali.qweatherapp.data.model.CitiesResponseDto
import com.mirzaali.qweatherapp.data.model.CityDto
import com.mirzaali.qweatherapp.domain.model.City
import java.util.Locale

fun CityDto.toDomain(): City {
    val isArabic = Locale.getDefault().language == "ar"

    return City(
        id = city_id,
        name = if (isArabic) name_ar else name,
        arabicName = name_ar,
        countryCode = country,
        countryName = if (isArabic) country_name_ar else country_name,
        arabicCountryName = country_name_ar,
        coordinates = City.Coordinates(
            latitude = latitude,
            longitude = longitude
        ),
        timezoneOffset = utc_offset
    )
}

fun List<CityDto>.toDomain(): List<City> = map { it.toDomain() }


fun CitiesResponseDto.toDomainCities(region: String? = null): List<City> {
    return when {
        region != null -> Response.result.cities[region].orEmpty().toDomain()
        else -> Response.result.cities.flatMap { (_, cities) -> cities.toDomain() }
    }
}

fun CitiesResponseDto.toRegionalDomains(): Pair<List<City>, List<City>> {
    val qatar = Response.result.cities["qatar"].orEmpty().toDomain()
    val world = Response.result.cities["world"].orEmpty().toDomain()
    return qatar to world
}