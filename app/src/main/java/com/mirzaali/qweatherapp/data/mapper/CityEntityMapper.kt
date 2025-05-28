package com.mirzaali.qweatherapp.data.mapper

import com.mirzaali.qweatherapp.data.local.CityEntity
import com.mirzaali.qweatherapp.domain.model.City
import java.util.Locale


fun CityEntity.toDomain(): City {
    val isArabic = Locale.getDefault().language == "ar"
    return City(
        id = cityId,
        name = if (isArabic) nameAr else name,
        arabicName = nameAr,
        countryCode = country,
        countryName = if (isArabic) countryNameAr else countryName,
        arabicCountryName = countryNameAr,
        coordinates = City.Coordinates(latitude, longitude),
        timezoneOffset = utcOffset
    )
}

fun City.toEntity(): CityEntity = CityEntity(
    id = id,
    cityId = id,
    name = name,
    nameAr = arabicName,
    country = countryCode,
    countryName = countryName,
    countryNameAr = arabicCountryName,
    latitude = coordinates.latitude,
    longitude = coordinates.longitude,
    utcOffset = timezoneOffset
)
