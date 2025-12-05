package com.moonsu.assignment.data.impl

import com.moonsu.assignment.core.datastore.LocalThemeDataSource
import com.moonsu.assignment.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeRepositoryImpl @Inject constructor(
    private val localThemeDataSource: LocalThemeDataSource,
) : ThemeRepository {
    override val isDarkTheme: Flow<Boolean?> = localThemeDataSource.isDarkTheme

    override suspend fun setDarkTheme(isDark: Boolean) {
        localThemeDataSource.setDarkTheme(isDark)
    }
}
