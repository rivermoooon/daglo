package com.moonsu.assignment.core.datastore.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.moonsu.assignment.core.datastore.LocalThemeDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")

@Singleton
class LocalThemeDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocalThemeDataSource {
    override val isDarkTheme: Flow<Boolean?> = context.themeDataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY]
        }

    override suspend fun setDarkTheme(isDark: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }
}
