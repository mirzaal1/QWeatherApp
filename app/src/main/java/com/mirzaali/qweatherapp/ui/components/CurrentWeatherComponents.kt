package com.mirzaali.qweatherapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.CurrentWeather
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.ui.theme.darkFont
import com.mirzaali.qweatherapp.ui.theme.lightFont
import com.mirzaali.qweatherapp.ui.theme.primary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun CurrentWeatherCard(forecast: WeatherForecast){
    val current = forecast.current
    val city = forecast.city
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = city.name,
                color = darkFont,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Text(
                text = SimpleDateFormat(
                    "EEE, d MMM, hh:mm a",
                    Locale.getDefault()
                ).format(Date()),
                color = lightFont,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                    ) {
                        Text(
                            text = "${current.temperature.toInt()}${current.temperatureUnit}",
                            color = darkFont,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = current.weatherType,
                            color = darkFont,
                            fontSize = 24.sp
                        )
                        Text(
                            text = "${stringResource(R.string.feels_like)} ${current.feelsLike.toInt()}${current.feelsLikeUnit}",
                            color = primary,
                            fontSize = 12.sp
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_main_cloud),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(180.dp)
                        .padding(end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_max_temp),
                        contentDescription = "Max Temp",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${current.temperatureMax.toInt()}${current.temperatureUnit}",
                        color = darkFont
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_min_temp),
                        contentDescription = "Min Temp",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${current.temperatureMin.toInt()}${current.temperatureUnit}",
                        color = darkFont
                    )
                }
            }
        }
    }
}


@Composable
fun WeatherInfoRow(current: CurrentWeather) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WeatherInfoCard(
            icon = painterResource(id = R.drawable.ic_percep),
            title = "PRECIP",
            value = "${current.rain}%",
            modifier = Modifier.weight(1f)
        )
        WeatherInfoCard(
            icon = painterResource(id = R.drawable.ic_wind_dir),
            title = "",
            value = getCompassDirection(current.windDirection),
            modifier = Modifier.weight(1f)
        )
        WeatherInfoCard(
            icon = painterResource(id = R.drawable.ic_clr_wind),
            title = "",
            value = "${current.windSpeed} ${current.windSpeedUnit}",
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
fun WeatherInfoCard(
    icon: Painter,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier
            .height(60.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Column {
                if (title.isNotEmpty()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                    )
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}


fun getCompassDirection(degrees: Int): String {
    val directions = listOf(
        " N ", "NNE", "NE", "ENE",
        " E ", "ESE", "SE", "SSE",
        " S ", "SSW", "SW", "WSW",
        " W ", "WNW", "NW", "NNW"
    )
    val index = ((degrees / 22.5) + 0.5).toInt() % 16
    return directions[index]
}

@Composable
fun getUvLevel(uvIndex: Double?): String {
    val context = LocalContext.current
    return when (uvIndex) {
        null -> context.getString(R.string.uv_index_unknown)
        in 0.0..2.9 -> context.getString(R.string.uv_index_low)
        in 3.0..5.9 -> context.getString(R.string.uv_index_moderate)
        in 6.0..7.9 -> context.getString(R.string.uv_index_high)
        in 8.0..10.9 -> context.getString(R.string.uv_index_very_high)
        else -> context.getString(R.string.uv_index_extreme)
    }
}

@Composable
fun WeatherDetailsList(current: CurrentWeather) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {


        Divider(color = Color.LightGray)

        WeatherDetailRow(
            icon = painterResource(id = R.drawable.ic_water_drop),
            label = stringResource(R.string.label_humidity),
            value = "${current.humidity}${current.humidityUnit}"
        )

        Divider(color = Color.LightGray)

        WeatherDetailRow(
            icon = painterResource(id = R.drawable.ic_uv),
            label = stringResource(R.string.label_uv_index),
            valueStyled = buildAnnotatedString {
                val uvLevel = getUvLevel(current.uvIndex.toDoubleOrNull())
                withStyle(SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                    append(uvLevel)
                }
                append(" ${current.uvIndex}")
            }
        )

        Divider(color = Color.LightGray)

        WeatherDetailRow(
            icon = painterResource(id = R.drawable.ic_pressure),
            label = stringResource(R.string.label_pressure),
            value = "${current.pressure} ${current.pressureUnit}"
        )

        Divider(color = Color.LightGray)

        WeatherDetailRow(
            icon = painterResource(id = R.drawable.ic_visibility),
            label = stringResource(R.string.label_visibility),
            value = "${current.visibility} ${current.visibilityUnit}"
        )

        Divider(color = Color.LightGray)
    }
}


@Composable
fun WeatherDetailRow(
    icon: Painter,
    label: String,
    value: String? = null,
    valueStyled: AnnotatedString? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = darkFont,
                fontWeight = FontWeight.Medium
            )
        }

        if (valueStyled != null) {
            Text(
                text = valueStyled,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        } else {
            Text(
                text = value ?: "",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


