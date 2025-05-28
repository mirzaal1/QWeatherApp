package com.mirzaali.qweatherapp.utils.localization

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocalizationUtils {

    @SuppressLint("ObsoleteSdkInt")
    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

}