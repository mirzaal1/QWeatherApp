package com.mirzaali.qweatherapp.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.utils.localization.LanguageDataStore
import kotlinx.coroutines.launch

@Composable
fun LanguageToggleButton(activity: Activity) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentLang by remember { mutableStateOf("en") }

    LaunchedEffect(Unit) {
        currentLang = LanguageDataStore.getLanguage(context)
    }

    val isArabic = currentLang == "ar"

    Button(onClick = {
        val newLang = if (isArabic) "en" else "ar"
        scope.launch {
            LanguageDataStore.saveLanguage(context, newLang)
            activity.recreate()
        }
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.language),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (isArabic) "English" else "العربية")
        }
    }
}
