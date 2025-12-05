package com.moonsu.assignment.core.datastore.di

import com.moonsu.assignment.core.datastore.LocalThemeDataSource
import com.moonsu.assignment.core.datastore.impl.LocalThemeDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalThemeDataSource(
        impl: LocalThemeDataSourceImpl,
    ): LocalThemeDataSource
}
