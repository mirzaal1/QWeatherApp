package com.mirzaali.qweatherapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.mirzaali.qweatherapp.BuildConfig
import com.mirzaali.qweatherapp.data.api.WeatherApiService
import com.mirzaali.qweatherapp.utils.DashToNullTypeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://apim.qweather.app/prod/api/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(Int::class.java, DashToNullTypeAdapter())
        .registerTypeAdapter(Int::class.javaObjectType, DashToNullTypeAdapter())
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            if (BuildConfig.DEBUG) {
                if (message.startsWith("{") || message.startsWith("[")) {
                    try {
                        val jsonElement = JsonParser().parse(message)
                        val prettyJson = GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
                        Timber.tag("OkHttp").i(prettyJson)
                    } catch (e: JsonSyntaxException) {
                        Timber.tag("OkHttp").e("Invalid JSON in response: $message")
                    }
                } else {
                    Timber.tag("OkHttp").i(message)
                }
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()



    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)



}