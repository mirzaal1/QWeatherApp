package com.mirzaali.qweatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.CityInfo
import com.mirzaali.qweatherapp.domain.model.CurrentWeather
import com.mirzaali.qweatherapp.utils.toFormattedTime


@Composable
fun CurrentWeatherCard(
    current: CurrentWeather,
    city: CityInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = current.weatherType,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${current.temperature}°",
                style = MaterialTheme.typography.displayLarge
            )

            Text(
                text = "${stringResource(R.string.feels_like)} ${current.feelsLike}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            WeatherInfoRow(
                items = listOf(
                    WeatherInfo(stringResource(R.string.wind), "${current.windSpeed} ${current.windSpeedUnit}", R.drawable.ic_wind),
                    WeatherInfo(stringResource(R.string.humidity), "${current.humidity}${current.humidityUnit}", R.drawable.ic_water_drop),
                    WeatherInfo(stringResource(R.string.visibility), "${current.visibility} ${current.visibilityUnit}", R.drawable.ic_visibility)
                )
            )

            WeatherInfoRow(
                items = listOf(
                    WeatherInfo(stringResource(R.string.min_temp), "${current.temperatureMin}${current.temperatureUnit}", R.drawable.ic_min_temp),
                    WeatherInfo(stringResource(R.string.max_temp), "${current.temperatureMax}${current.temperatureUnit}", R.drawable.ic_max_temp),
                    WeatherInfo(stringResource(R.string.rain), "${current.rain} ${current.rainUnit}", R.drawable.ic_rain)
                )
            )

            WeatherInfoRow(
                items = listOf(
                    WeatherInfo(stringResource(R.string.pressure), "${current.pressure} ${current.pressureUnit}", R.drawable.ic_pressure),
                    WeatherInfo(stringResource(R.string.clouds), "${current.clouds}%", R.drawable.ic_cloud),
                    WeatherInfo(stringResource(R.string.uv_index), current.uvIndex, R.drawable.ic_uv)
                )
            )

            WeatherInfoRow(
                items = listOf(
                    WeatherInfo(stringResource(R.string.sunrise), current.sunrise.toFormattedTime(), R.drawable.ic_sunrise),
                    WeatherInfo(stringResource(R.string.sunset), current.sunset.toFormattedTime(), R.drawable.ic_sunset)
                )
            )
        }
    }
}


@Composable
fun WeatherInfoRow(items: List<WeatherInfo>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach {
            WeatherDetailItem(
                icon = painterResource(id = it.iconRes),
                value = it.value,
                label = it.label
            )
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
            modifier = Modifier.size(24.dp),
            tint = Color.Unspecified
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        Text(text = label, style = MaterialTheme.typography.labelMedium)
    }
}


data class WeatherInfo(
    val label: String,
    val value: String,
    val iconRes: Int
)
