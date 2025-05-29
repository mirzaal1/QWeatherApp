package com.mirzaali.qweatherapp.ui.main.current_weather

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.HourlyWeather
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.ui.components.CurrentWeatherCard
import com.mirzaali.qweatherapp.ui.components.LocationPickerBottomSheet
import com.mirzaali.qweatherapp.ui.components.WeatherDetailsList
import com.mirzaali.qweatherapp.ui.components.WeatherInfoRow
import com.mirzaali.qweatherapp.ui.components.getCompassDirection
import com.mirzaali.qweatherapp.ui.components.roundedContainer
import com.mirzaali.qweatherapp.ui.theme.BlackAlpha
import com.mirzaali.qweatherapp.ui.theme.blueFont
import com.mirzaali.qweatherapp.ui.theme.lightBg
import com.mirzaali.qweatherapp.ui.theme.primary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val currentLocale = LocalConfiguration.current.locale
    val forecastState = viewModel.forecastState.value
    val citiesState = viewModel.citiesState.value
    var showLocationPicker by rememberSaveable { mutableStateOf(false) }
    var initialLoadDone by rememberSaveable { mutableStateOf(false) }
    var autoOpenedSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(currentLocale) {
        if (initialLoadDone) {
            viewModel.reloadDataWithNewLocale(currentLocale)
        }
    }

    LaunchedEffect(initialLoadDone) {
        if (!initialLoadDone) {
            viewModel.loadCities()
            viewModel.loadLastCityForecast()
            initialLoadDone = true
        }
    }

    val cities = (citiesState as? WeatherUiState.Success)?.data
    LaunchedEffect(cities, forecastState) {
        if (!autoOpenedSheet && forecastState !is WeatherUiState.Success && !cities.isNullOrEmpty()) {
            showLocationPicker = true
            autoOpenedSheet = true
        }
    }

    if (showLocationPicker && !cities.isNullOrEmpty()) {
        LocationPickerBottomSheet(
            cities = cities,
            onDismiss = { showLocationPicker = false },
            onCitySelected = { city ->
                viewModel.saveSelectedCity(city.id)
                viewModel.loadForecast(city.id)
                showLocationPicker = false
            }
        )
    }

    Scaffold(
        topBar = {
            WeatherTopAppBar(
                title = if (forecastState is WeatherUiState.Success) forecastState.data.city.name else stringResource(
                    R.string.selectcity
                ),
                onMenuClick = onMenuClick,
                onLocationClick = { showLocationPicker = true }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (forecastState) {
                is WeatherUiState.Idle, is WeatherUiState.Loading -> if (!showLocationPicker) WeatherLoadingScreen()

                is WeatherUiState.Error -> WeatherErrorScreen(
                    message = forecastState.message ?: stringResource(R.string.error_unknown),
                    onRetry = { viewModel.loadLastCityForecast() }
                )

                is WeatherUiState.Success ->
                    DetailedForecastCardFromModel(
                        forecastState.data,
                        onPreviousClick = {},
                        onNextClick = {})
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherTopAppBar(
    title: String,
    onMenuClick: () -> Unit,
    onLocationClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(BlackAlpha, shape = RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable { onLocationClick() }
            ) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Location",
                    tint = Color.Red,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(60.dp))
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = stringResource(R.string.menu),
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF4A0D36),
            titleContentColor = Color.White
        )
    )
}

@Composable
fun WeatherLoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun WeatherErrorScreen(message: String,
                       onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
fun DetailedForecastCardFromModel(
    forecast: WeatherForecast,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val current = forecast.current
    val city = forecast.city

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_detailed_forecast),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onPreviousClick,
                            modifier = Modifier.roundedContainer(lightBg)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = "Prev"
                            )
                        }

                        Text(
                            text = SimpleDateFormat(
                                "dd MMM, yyyy",
                                Locale.getDefault()
                            ).format(Date()),
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

                    Spacer(modifier = Modifier.height(16.dp))
                    CurrentWeatherCard(forecast)
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherInfoRow(current)
                    Spacer(modifier = Modifier.height(12.dp))
                    WeatherDetailsList(current)
                    Spacer(modifier = Modifier.height(24.dp))
                    HourlyForecastSection(forecast)
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
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
fun HourlyForecastSection(forecast: WeatherForecast) {
    val hourlyData = forecast.hourly
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.label_hourly_forecast),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = blueFont
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(hourlyData.take(12)) { hour ->
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
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .width(90.dp)
            .height(160.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = timeLabel,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF272742)
            )
            Spacer(modifier = Modifier.height(6.dp))

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Color(0xFFD9AD5B)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${hour.temperature.toInt()}°C",
                fontSize = 16.sp,
                color = Color(0xFF9D1C38),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_wind_dir),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = getCompassDirection(hour.windDirection),
                color = Color(0xFF272742),
                fontSize = 12.sp
            )

            Text(
                text = "${hour.windSpeed.toInt()} km/h",
                color = Color(0xFF272742),
                fontSize = 12.sp
            )
        }
    }
}


@Composable
private fun WeatherContent(
    forecast: WeatherForecast,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {

        /* CurrentWeatherCard(
             current = forecast.current,
             city = forecast.city
         ) {
             onCardClick()
         }*/

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.hourly_forecast),
            style = MaterialTheme.typography.titleLarge
        )

        HourlyForecastList(forecast.hourly)

    }
}

@Composable
fun HourlyForecastList(hourly: List<HourlyWeather>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hourly) { hour ->
            HourlyForecastItem(hour)
        }
    }
}

@Composable
fun HourlyForecastItem(hour: HourlyWeather) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = hour.time, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "${hour.temperature.toInt()}°", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = hour.weatherType, style = MaterialTheme.typography.bodySmall)
        }
    }
}
