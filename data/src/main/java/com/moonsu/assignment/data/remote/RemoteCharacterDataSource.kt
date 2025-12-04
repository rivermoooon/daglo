package com.moonsu.assignment.data.remote

import com.moonsu.assignment.data.model.CharacterEntity

interface RemoteCharacterDataSource {
    suspend fun getCharacters(page: Int): List<CharacterEntity>
    suspend fun getCharacter(id: Int): CharacterEntity
    suspend fun searchCharacters(name: String): List<CharacterEntity>
}
