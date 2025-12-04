package com.moonsu.assignment.core.network.di

import com.moonsu.assignment.core.network.impl.RemoteCharacterDataSourceImpl
import com.moonsu.assignment.data.remote.RemoteCharacterDataSource
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
    abstract fun bindRemoteCharacterDataSource(
        impl: RemoteCharacterDataSourceImpl,
    ): RemoteCharacterDataSource
}
