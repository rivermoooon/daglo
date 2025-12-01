package com.moonsu.assignment.core.network.service

import com.moonsu.assignment.core.network.model.CharacterDto
import com.moonsu.assignment.core.network.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(CHARACTERS)
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
    ): CharacterResponse

    @GET(CHARACTER_DETAIL)
    suspend fun getCharacter(
        @Path("id") id: Int,
    ): CharacterDto

    companion object {
        const val CHARACTERS = "character"
        const val CHARACTER_DETAIL = "character/{id}"
    }
}
