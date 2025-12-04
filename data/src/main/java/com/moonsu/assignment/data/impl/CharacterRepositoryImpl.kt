package com.moonsu.assignment.data.impl

import com.moonsu.assignment.core.common.di.IoDispatcher
import com.moonsu.assignment.data.flowDataResource
import com.moonsu.assignment.data.remote.RemoteCharacterDataSource
import com.moonsu.assignment.data.toDomainList
import com.moonsu.assignment.domain.DataResource
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remote: RemoteCharacterDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CharacterRepository {
    override fun getCharacters(page: Int): Flow<DataResource<List<Character>>> =
        flowDataResource {
            remote.getCharacters(page).toDomainList()
        }.flowOn(ioDispatcher)
}
