package com.mirzaali.qweatherapp.utils.localization


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.languageDataStore by preferencesDataStore(name = "language_prefs")

object LanguageDataStore {
    private val LANGUAGE_KEY = stringPreferencesKey("app_language")

    suspend fun saveLanguage(context: Context, language: String) {
        context.languageDataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }

    suspend fun getLanguage(context: Context): String {
        return context.languageDataStore.data
            .map { it[LANGUAGE_KEY] ?: "en" }
            .first()
    }
}
