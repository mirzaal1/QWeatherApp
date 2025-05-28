package com.mirzaali.qweatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mirzaali.qweatherapp.ui.main.current_weather.MainScreen
import com.mirzaali.qweatherapp.ui.main.forecast.ForecastScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable(route = "main") {
            MainScreen(viewModel = hiltViewModel(), onMenuClick = {}, onCardClick = {
                navController.navigate("forecast")
            })
        }

        composable("forecast") {
            ForecastScreen(viewModel = hiltViewModel(), onBackPress = {
                navController.popBackStack()
            })
        }
    }
}
