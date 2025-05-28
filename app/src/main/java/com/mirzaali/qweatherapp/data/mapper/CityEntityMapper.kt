package com.mirzaali.qweatherapp.data.mapper

import com.mirzaali.qweatherapp.data.local.CityEntity
import com.mirzaali.qweatherapp.domain.model.City


fun CityEntity.toDomain(): City = City(
    id = cityId,
    name = name,
    arabicName = nameAr,
    countryCode = country,
    countryName = countryName,
    arabicCountryName = countryNameAr,
    coordinates = City.Coordinates(latitude, longitude),
    timezoneOffset = utcOffset
)

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
