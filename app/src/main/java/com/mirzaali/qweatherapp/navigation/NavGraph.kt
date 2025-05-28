package com.mirzaali.qweatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mirzaali.qweatherapp.ui.main.ForecastScreen
import com.mirzaali.qweatherapp.ui.main.MainScreen

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
            MainScreen(onMenuClick = {}, onCardClick = {
                navController.navigate("forecast")
            })
        }

        composable("forecast") {
            ForecastScreen(onBackPress = {
                navController.popBackStack()
            })
        }
    }
}
