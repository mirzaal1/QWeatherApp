package com.mirzaali.qweatherapp.ui.main

sealed class LoadStatus {
    data object Idle : LoadStatus()
    data object Loading : LoadStatus()
    data class Error(val message: String?) : LoadStatus()
}
