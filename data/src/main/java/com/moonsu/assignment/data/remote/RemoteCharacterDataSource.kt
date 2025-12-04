package com.moonsu.assignment.data.remote

import com.moonsu.assignment.data.model.CharacterEntity
import com.moonsu.assignment.data.model.CharacterPageEntity

interface RemoteCharacterDataSource {
    suspend fun getCharacters(page: Int): CharacterPageEntity
    suspend fun getCharacter(id: Int): CharacterEntity
    suspend fun searchCharacters(name: String): CharacterPageEntity
}
