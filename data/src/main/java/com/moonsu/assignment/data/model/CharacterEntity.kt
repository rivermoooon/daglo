package com.moonsu.assignment.data.model

import com.moonsu.assignment.data.DataMapper
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.domain.model.Location
import com.moonsu.assignment.domain.model.Origin

data class CharacterEntity(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginEntity,
    val location: LocationEntity,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
) : DataMapper<Character> {
    override fun toDomain() = Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin.toDomain(),
        location = location.toDomain(),
        image = image,
        episode = episode,
        url = url,
        created = created,
    )
}

data class OriginEntity(
    val name: String,
    val url: String,
) : DataMapper<Origin> {
    override fun toDomain() = Origin(
        name = name,
        url = url,
    )
}

data class LocationEntity(
    val name: String,
    val url: String,
) : DataMapper<Location> {
    override fun toDomain() = Location(
        name = name,
        url = url,
    )
}

data class PageInfoEntity(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)

data class CharacterPageEntity(
    val info: PageInfoEntity,
    val results: List<CharacterEntity>,
)
