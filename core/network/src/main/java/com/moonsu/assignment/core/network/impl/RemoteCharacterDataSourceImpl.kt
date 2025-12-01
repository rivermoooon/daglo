package com.moonsu.assignment.core.network.impl

import com.moonsu.assignment.core.network.service.ApiService
import com.moonsu.assignment.data.datasource.RemoteCharacterDataSource
import com.moonsu.assignment.data.model.CharacterEntity
import javax.inject.Inject

class RemoteCharacterDataSourceImpl @Inject constructor(
    private val api: ApiService,
) : RemoteCharacterDataSource {

    override suspend fun getCharacters(page: Int): List<CharacterEntity> =
        api.getCharacters(page).results.map { it.toData() }

    override suspend fun getCharacter(id: Int): CharacterEntity =
        api.getCharacter(id).toData()
}
