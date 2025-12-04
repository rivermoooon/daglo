package com.moonsu.assignment.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.moonsu.assignment.core.common.di.DefaultDispatcher
import com.moonsu.assignment.core.common.di.IoDispatcher
import com.moonsu.assignment.data.flowDataResource
import com.moonsu.assignment.data.paging.CharacterPagingSource
import com.moonsu.assignment.data.remote.RemoteCharacterDataSource
import com.moonsu.assignment.data.toDomainList
import com.moonsu.assignment.domain.DataResource
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remote: RemoteCharacterDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : CharacterRepository {
    override fun getCharacters(page: Int): Flow<DataResource<List<Character>>> =
        flowDataResource {
            remote.getCharacters(page).toDomainList()
        }.flowOn(ioDispatcher)

    override fun getPagedCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40,
                prefetchDistance = 2,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                CharacterPagingSource(
                    remoteDataSource = remote,
                    ioDispatcher = ioDispatcher,
                )
            },
        ).flow
            .map { pagingData -> pagingData.map { it.toDomain() } }
            .flowOn(defaultDispatcher)
    }

    override fun getCharacter(id: Int): Flow<DataResource<Character>> =
        flowDataResource {
            remote.getCharacter(id).toDomain()
        }.flowOn(ioDispatcher)

    override fun searchCharacters(name: String): Flow<DataResource<List<Character>>> =
        flowDataResource {
            remote.searchCharacters(name).toDomainList()
        }.flowOn(ioDispatcher)
}
