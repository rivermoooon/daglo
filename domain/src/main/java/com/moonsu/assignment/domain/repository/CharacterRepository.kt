package com.moonsu.assignment.domain.repository

import com.moonsu.assignment.domain.DataResource
import com.moonsu.assignment.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(page: Int): Flow<DataResource<List<Character>>>
    fun getCharacter(id: Int): Flow<DataResource<Character>>
    fun searchCharacters(name: String): Flow<DataResource<List<Character>>>
}
