package com.mirzaali.qweatherapp.di

import android.content.Context
import androidx.room.Room
import com.mirzaali.qweatherapp.data.local.WeatherDao
import com.mirzaali.qweatherapp.data.local.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideDao(db: WeatherDatabase): WeatherDao = db.weatherDao()
}
