package com.mirzaali.qweatherapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.DailyWeather
import com.mirzaali.qweatherapp.domain.model.HourlyWeather
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.ui.theme.blueFont
import com.mirzaali.qweatherapp.ui.theme.darkFont
import com.mirzaali.qweatherapp.ui.theme.lightBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




@Composable
fun DateNavigationRow(
    date: Date = Date(),
    modifier: Modifier,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp)
        ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onPreviousClick,
            modifier = Modifier.roundedContainer(lightBg)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Previous"
            )
        }

        Text(
            text = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(date),
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .roundedContainer(lightBg)
                .padding(horizontal = 40.dp, vertical = 12.dp)
        )

        IconButton(
            onClick = onNextClick,
            modifier = Modifier.roundedContainer(lightBg)
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "Next"
            )
        }
    }
}



@Composable
fun HourlyForecastSection(forecast: WeatherForecast,
                          modifier: Modifier = Modifier) {
    val hourlyData = forecast.hourly
    Column(modifier = modifier.then(Modifier)) {
        Text(
            text = stringResource(R.string.label_hourly_forecast),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = blueFont
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow() {
            items(hourlyData) { hour ->
                HourlyForecastCard(hour)
            }
        }
    }
}

@Composable
fun HourlyForecastCard(hour: HourlyWeather) {
    val iconRes = getWeatherIcon(hour.weatherIcon)
    val timeLabel = hour.time

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .width(90.dp)
            .height(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = timeLabel,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF272742)
            )

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFFD9AD5B)
            )

            Text(
                text = "${hour.temperature.toInt()}${hour.temperatureUnit}",
                fontSize = 16.sp,
                color = Color(0xFF9D1C38),
                fontWeight = FontWeight.Bold
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_wind_dir),
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = getCompassDirection(hour.windDirection),
                    color = Color(0xFF272742),
                    fontSize = 12.sp
                )
            }

            Text(
                text = "${hour.windSpeed.toInt()} ${hour.windPowerUnit}",
                color = Color(0xFF272742),
                fontSize = 12.sp
            )
        }
    }
}

@DrawableRes
fun getWeatherIcon(iconCode: String?): Int {
    return when (iconCode) {
        "01d", "01n" -> R.drawable.ic_sun
        "02d", "02n" -> R.drawable.ic_cloud_sun
        "03d", "03n", "04d", "04n" -> R.drawable.ic_cloud
        "09d", "09n", "10d", "10n" -> R.drawable.ic_thunder
        "11d", "11n" -> R.drawable.ic_thunder
        "13d", "13n" -> R.drawable.ic_thunder
        "50d", "50n" -> R.drawable.ic_thunder
        else -> R.drawable.ic_cloud
    }
}


@Composable
fun DailyForecastSection(dailyList: List<DailyWeather>,
                         modifier: Modifier = Modifier) {
    Column(modifier = modifier.then(Modifier)) {
        Text(
            text = stringResource(R.string.label_daily_forecast),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF0081C9),
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )

        dailyList.forEachIndexed { index, day ->
            val backgroundColor = if (index % 2 == 0) Color.White else Color(0xFFF9F9F9)
            DailyForecastRow(day, backgroundColor)
        }
    }
}

@Composable
fun DailyForecastRow(day: DailyWeather,
                     backgroundColor: Color) {
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(day.timestamp * 1000))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = dayOfWeek,
                fontWeight = FontWeight.Medium,
                color = darkFont,
                fontSize = 16.sp
            )
            Text(
                text = "${day.temperatureMax.toInt()}${day.feelsLikeUnit}",
                color = darkFont,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = getWeatherIcon(day.weatherIcon)),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = day.weatherType,
                color = Color.Black,
                fontSize = 14.sp
            )

            Text(
                text = "${day.temperatureMax.toInt()}/${day.temperatureMin.toInt()}${day.feelsLikeUnit}",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}
