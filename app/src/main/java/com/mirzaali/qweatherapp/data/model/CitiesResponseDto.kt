package com.mirzaali.qweatherapp.data.model


data class CitiesResponseDto(
    val Response: CityResponseWrapper
)

data class CityResponseWrapper(
    val status: Boolean,
    val result: CityResult
)

data class CityResult(
    val cities: CitiesByRegion
)

data class CitiesByRegion(
    val qatar: List<CityDto>,
    val world: List<CityDto>
)

data class CityDto(
    val city_id: Int,
    val name: String,
    val country: String,
    val country_name: String,
    val country_name_ar: String,
    val latitude: Double,
    val longitude: Double,
    val utc_offset: String,
    val name_ar: String
)