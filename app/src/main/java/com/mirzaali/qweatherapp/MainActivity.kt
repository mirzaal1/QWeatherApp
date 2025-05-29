package com.mirzaali.qweatherapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mirzaali.qweatherapp.navigation.NavGraph
import com.mirzaali.qweatherapp.ui.theme.QWeatherAppTheme
import com.mirzaali.qweatherapp.utils.SetStatusBarColor
import com.mirzaali.qweatherapp.utils.localization.LanguageDataStore
import com.mirzaali.qweatherapp.utils.localization.LocalizationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QWeatherAppTheme {

                SetStatusBarColor(
                    color = colorScheme.primary,
                    darkIcons = false
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.safeDrawing.asPaddingValues())
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController)
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val lang = runBlocking { LanguageDataStore.getLanguage(newBase) }
        val localizedContext = LocalizationUtils.setLocale(newBase, lang)
        super.attachBaseContext(localizedContext)
    }

}