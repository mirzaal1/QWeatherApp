package com.mirzaali.qweatherapp.di

import com.mirzaali.qweatherapp.data.repository.WeatherRepository
import com.mirzaali.qweatherapp.domain.usecase.GetCitiesUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetWeatherForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository = impl

    @Provides
    @Singleton
    fun provideGetCitiesUseCase(
        repo: WeatherRepository
    ): GetCitiesUseCase = GetCitiesUseCase(repo)

    @Provides
    @Singleton
    fun provideGetWeatherForecastUseCase(
        repo: WeatherRepository
    ): GetWeatherForecastUseCase = GetWeatherForecastUseCase(repo)
}