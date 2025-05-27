package com.mirzaali.qweatherapp.utils

import android.content.Context
import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mirzaali.qweatherapp.R
import java.util.Locale

object LocalizationUtils {
    fun toggleLanguage(context: Context) {
        val newLocale = when(Locale.getDefault().language) {
            "ar" -> Locale("en")
            else -> Locale("ar")
        }

        val config = Configuration(context.resources.configuration)
        config.setLocale(newLocale)

        context.createConfigurationContext(config)
        Locale.setDefault(newLocale)
    }
}

@Composable
fun LanguageSwitchButton() {
    val context = LocalContext.current
    IconButton(onClick = { LocalizationUtils.toggleLanguage(context) }) {
        Icon(
            painter = painterResource(id = R.drawable.language),
            contentDescription = stringResource(R.string.change_language)
        )
    }
}