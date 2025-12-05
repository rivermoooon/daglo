package com.moonsu.assignment.core.datastore

import kotlinx.coroutines.flow.Flow

interface LocalThemeDataSource {
    val isDarkTheme: Flow<Boolean?>
    suspend fun setDarkTheme(isDark: Boolean)
}
