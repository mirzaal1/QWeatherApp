package com.mirzaali.qweatherapp.domain.model

class City(
    val id: Int,
    val name: String,
    val nameAr: String,
    val country: String,
    val countryName: String,
    val countryNameAr: String,
    val latitude: Double,
    val longitude: Double,
    val utcOffset: String
)