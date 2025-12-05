package com.moonsu.assignment.data.di

import com.moonsu.assignment.core.common.di.DefaultDispatcher
import com.moonsu.assignment.core.common.di.IoDispatcher
import com.moonsu.assignment.core.datastore.LocalThemeDataSource
import com.moonsu.assignment.data.impl.CharacterRepositoryImpl
import com.moonsu.assignment.data.impl.ThemeRepositoryImpl
import com.moonsu.assignment.data.remote.RemoteCharacterDataSource
import com.moonsu.assignment.domain.repository.CharacterRepository
import com.moonsu.assignment.domain.repository.ThemeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(
        remote: RemoteCharacterDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    ): CharacterRepository = CharacterRepositoryImpl(remote, ioDispatcher, defaultDispatcher)

    @Provides
    @Singleton
    fun provideThemeRepository(
        localThemeDataSource: LocalThemeDataSource,
    ): ThemeRepository = ThemeRepositoryImpl(localThemeDataSource)
}
