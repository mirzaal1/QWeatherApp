package com.mirzaali.qweatherapp.domain.model

data class City(
    val id: Int,
    val name: String,
    val arabicName: String,
    val countryCode: String,
    val countryName: String,
    val arabicCountryName: String,
    val coordinates: Coordinates,
    val timezoneOffset: String
) {
    data class Coordinates(
        val latitude: Double,
        val longitude: Double
    )
}