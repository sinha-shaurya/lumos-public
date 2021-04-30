package com.example.lumos.repository

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val THEME_MODE = "ui_theme"

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private val TAG = "UserPreferencesRepository"

    //define keys for datastore
    private object PreferencesKeys {
        val theme = intPreferencesKey(THEME_MODE)
    }

    val userPreferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error in reading user preferences", exception)
                emit(emptyPreferences())
            } else
                throw exception
        }
        .map { value ->
            val preference = value[PreferencesKeys.theme] ?: AppCompatDelegate.MODE_NIGHT_YES
            preference
        }

    suspend fun changeTheme(theme: Int) =
        dataStore.edit {
            it[PreferencesKeys.theme] = theme
        }

}