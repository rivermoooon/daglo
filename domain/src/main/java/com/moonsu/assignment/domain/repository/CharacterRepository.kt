package com.moonsu.assignment.domain.repository

import androidx.paging.PagingData
import com.moonsu.assignment.domain.DataResource
import com.moonsu.assignment.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getPagedCharacters(): Flow<PagingData<Character>>
    fun getCharacter(id: Int): Flow<DataResource<Character>>
    fun searchCharacters(name: String): Flow<DataResource<List<Character>>>
}
