package com.mirzaali.qweatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.CityInfo
import com.mirzaali.qweatherapp.domain.model.CurrentWeather

@Composable
fun CurrentWeatherCard(
    current: CurrentWeather,
    city: CityInfo,
    isRtl: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (isRtl && city.nameAr.isNotEmpty()) city.nameAr else city.name,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "${current.temperature}°",
                style = MaterialTheme.typography.displayLarge
            )

            Text(
                text = if (isRtl) current.weatherTypeAr else current.weatherType,
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetailItem(
                    icon = painterResource(id = R.drawable.ic_water_drop),
                    value = "${current.humidity}%",
                    label = stringResource(R.string.humidity)
                )

                WeatherDetailItem(
                    icon =painterResource(id = R.drawable.wind),
                    value = "${current.windSpeed} m/s",
                    label = stringResource(R.string.wind)
                )

                WeatherDetailItem(
                    icon = painterResource(id = R.drawable.visibility),
                    value = "${current.visibility} m",
                    label = stringResource(R.string.visibility)
                )
            }
        }
    }
}


@Composable
fun WeatherDetailItem(
    icon: Painter,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}
