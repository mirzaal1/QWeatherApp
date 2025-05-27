package com.mirzaali.qweatherapp.data.mapper

import com.mirzaali.qweatherapp.data.model.CityDto
import com.mirzaali.qweatherapp.domain.model.City


fun CityDto.toDomain(): City {
    return City(
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