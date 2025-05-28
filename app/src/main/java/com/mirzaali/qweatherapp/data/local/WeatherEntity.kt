package com.mirzaali.qweatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Int,
    val cityId: Int,
    val name: String,
    val nameAr: String,
    val country: String,
    val countryName: String,
    val countryNameAr: String,
    val latitude: Double,
    val longitude: Double,
    val utcOffset: String
)

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey val cityId: Int,
    val forecastJson: String,
    val timestamp: Long
)
