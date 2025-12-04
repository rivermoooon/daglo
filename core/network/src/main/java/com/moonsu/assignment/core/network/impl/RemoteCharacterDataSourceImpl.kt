package com.moonsu.assignment.core.network.impl

import com.moonsu.assignment.core.network.service.ApiService
import com.moonsu.assignment.data.model.CharacterEntity
import com.moonsu.assignment.data.model.CharacterPageEntity
import com.moonsu.assignment.data.remote.RemoteCharacterDataSource
import javax.inject.Inject

class RemoteCharacterDataSourceImpl @Inject constructor(
    private val api: ApiService,
) : RemoteCharacterDataSource {

    override suspend fun getCharacters(page: Int): CharacterPageEntity =
        api.getCharacters(page).toData()

    override suspend fun getCharacter(id: Int): CharacterEntity =
        api.getCharacter(id).toData()

    override suspend fun searchCharacters(name: String): CharacterPageEntity =
        api.searchCharacters(name).toData()
}
