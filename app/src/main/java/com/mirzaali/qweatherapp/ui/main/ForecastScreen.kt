package com.mirzaali.qweatherapp.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.DailyWeather
import com.mirzaali.qweatherapp.ui.components.TopBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ForecastScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onBackPress: () -> Unit
) {

    val state = viewModel.uiState.value

    if (state is WeatherUiState.ForecastLoaded) {
        val forecast = state.forecast
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopBar(
                    title = stringResource(R.string.daily_forecast),
                    onBackClick = { onBackPress() }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(forecast.daily) { daily ->
                    DailyForecastItem(daily = daily)
                }
            }
        }
    } else {
        WeatherLoadingScreen()
    }
}


@Composable
fun DailyForecastItem(daily: DailyWeather) {
    val context = LocalContext.current
    val iconResId = context.resources.getIdentifier(
        daily.weatherIcon.lowercase(), "drawable", context.packageName
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = daily.timestamp.formatForecastDate(),
                style = MaterialTheme.typography.bodyLarge
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (iconResId != 0) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = daily.weatherType,
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = daily.weatherType)
                    Text(
                        text = "${daily.temperatureMin}° / ${daily.temperatureMax}°",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}


fun Long.formatForecastDate(): String {
    val sdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
    return sdf.format(Date(this * 1000))
}

