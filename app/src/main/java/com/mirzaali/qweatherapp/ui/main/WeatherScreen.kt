package com.mirzaali.qweatherapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.HourlyWeather
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.ui.components.CurrentWeatherCard
import com.mirzaali.qweatherapp.ui.components.LanguageToggleButton
import com.mirzaali.qweatherapp.ui.components.LocationPickerBottomSheet

@Composable
fun MainScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
    onCardClick: () -> Unit
) {
    val uiState = viewModel.uiState.value
    var showLocationPicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCities()
        viewModel.loadLastCityForecast()
    }

    if (showLocationPicker) {
        when (uiState) {
            is WeatherUiState.CitiesLoaded -> {
                LocationPickerBottomSheet(
                    cities = uiState.cities,
                    onDismiss = { showLocationPicker = false },
                    onCitySelected = { city ->
                        viewModel.saveSelectedCity(city.id)
                        viewModel.loadForecast(city.id)
                        showLocationPicker = false
                    }
                )
            }

            else -> {
                showLocationPicker = false // Dismiss if not ready
            }
        }
    }

    Scaffold(
        topBar = {
            WeatherTopAppBar(
                title = when (uiState) {
                    is WeatherUiState.ForecastLoaded -> uiState.forecast.city.name
                    else -> stringResource(R.string.app_name)
                },
                onMenuClick = onMenuClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showLocationPicker = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = stringResource(R.string.select_location)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (uiState) {
                WeatherUiState.Idle -> WeatherEmptyScreen(
                    message = stringResource(R.string.loading)
                )

                WeatherUiState.Loading -> WeatherLoadingScreen()

                is WeatherUiState.Error -> WeatherErrorScreen(
                    message = uiState.message ?: stringResource(R.string.error_unknown),
                    onRetry = { viewModel.loadCities() }
                )

                is WeatherUiState.ForecastLoaded -> WeatherContent(
                    forecast = uiState.forecast,
                    modifier = Modifier.fillMaxSize()
                ) {
                    onCardClick()
                }

                is WeatherUiState.CitiesLoaded -> WeatherEmptyScreen(
                    message = stringResource(R.string.select_location_prompt)
                )
            }
        }
    }
}

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherTopAppBar(
    title: String,
    onMenuClick: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
        actions = {
            activity?.let {
                LanguageToggleButton(it)
            }
        }
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
fun WeatherEmptyScreen(message: String = stringResource(R.string.no_data)) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
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

        CurrentWeatherCard(
            current = forecast.current,
            city = forecast.city
        ) {
            onCardClick()
        }

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


/*
 Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.daily_forecast),
            style = MaterialTheme.typography.titleLarge
        )

        DailyForecastList(forecast.daily)

        Spacer(modifier = Modifier.height(24.dp))

        // Hourly Forecast
        Text(
            text = stringResource(R.string.hourly_forecast),
            style = MaterialTheme.typography.titleLarge
        )
        HourlyForecastList(forecast.hourly)

@Composable
fun DailyForecastList(daily: List<DailyWeather>) {
    val dateFormat = remember { SimpleDateFormat("EEE, MMM d", Locale.getDefault()) }

    Column {
        daily.forEach { day ->
            val dateString = dateFormat.format(Date(day.timestamp * 1000))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = dateString)

                    Text(text = "${day.temperatureMin.toInt()}° / ${day.temperatureMax.toInt()}°")

                    Text(text = day.weatherType)
                }
            }
        }
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
    }*/

