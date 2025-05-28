package com.mirzaali.qweatherapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class CityPreferenceDataStore(private val context: Context) {

    companion object {
        private val SELECTED_CITY_ID_KEY = intPreferencesKey("selected_city_id")
    }

    val selectedCityIdFlow: Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_CITY_ID_KEY]
        }

    suspend fun saveSelectedCityId(cityId: Int) {
        context.dataStore.edit { preferences ->
            preferences[SELECTED_CITY_ID_KEY] = cityId
        }
    }

    suspend fun clearSelectedCityId() {
        context.dataStore.edit { preferences ->
            preferences.remove(SELECTED_CITY_ID_KEY)
        }
    }
}
