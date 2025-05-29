package com.mirzaali.qweatherapp.di

import android.content.Context
import com.mirzaali.qweatherapp.data.local.CityPreferenceDataStore
import com.mirzaali.qweatherapp.domain.repository.WeatherRepository
import com.mirzaali.qweatherapp.data.repository.WeatherRepositoryImpl
import com.mirzaali.qweatherapp.domain.usecase.GetCitiesUseCase
import com.mirzaali.qweatherapp.domain.usecase.GetWeatherForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun provideCityPreferenceDataStore(
        @ApplicationContext context: Context
    ): CityPreferenceDataStore {
        return CityPreferenceDataStore(context)
    }
}