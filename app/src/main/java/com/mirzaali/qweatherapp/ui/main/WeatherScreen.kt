package com.mirzaali.qweatherapp.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mirzaali.qweatherapp.R
import com.mirzaali.qweatherapp.domain.model.WeatherForecast
import com.mirzaali.qweatherapp.ui.components.CurrentWeatherCard
import com.mirzaali.qweatherapp.ui.components.DailyForecastSection
import com.mirzaali.qweatherapp.ui.components.DateNavigationRow
import com.mirzaali.qweatherapp.ui.components.HourlyForecastSection
import com.mirzaali.qweatherapp.ui.components.LanguageToggleButton
import com.mirzaali.qweatherapp.ui.components.LocationPickerBottomSheet
import com.mirzaali.qweatherapp.ui.components.WeatherDetailsList
import com.mirzaali.qweatherapp.ui.components.WeatherInfoRow
import com.mirzaali.qweatherapp.ui.theme.BlackAlpha
import com.mirzaali.qweatherapp.ui.theme.primary
import java.util.Date

@SuppressLint("ContextCastToActivity")
@Composable
fun MainScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onMenuClick: () -> Unit
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
                languageButton = {LanguageToggleButton(activity = LocalContext.current as Activity)},
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
                        forecastState.data
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherTopAppBar(
    title: String,
    languageButton: @Composable () -> Unit,
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
        actions = {
            languageButton()
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
    contentPadding: PaddingValues = PaddingValues()
) {
    val current = forecast.current
    var currentIndex by rememberSaveable { mutableStateOf(0) }
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
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = contentPadding.calculateBottomPadding())
                ) {

                    Spacer(modifier = Modifier.height(16.dp))

                    DateNavigationRow(
                        date = Date(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onPreviousClick = {},
                        onNextClick = {}
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CurrentWeatherCard(forecast, modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    WeatherInfoRow(current, modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    WeatherDetailsList(current)

                    Spacer(modifier = Modifier.height(24.dp))

                    HourlyForecastSection(forecast, modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(24.dp))

                    DailyForecastSection(forecast.daily)

                }
            }
        }
    }
}

