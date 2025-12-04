package com.moonsu.assignment.core.network.model

import com.moonsu.assignment.core.network.RemoteMapper
import com.moonsu.assignment.data.model.CharacterEntity
import com.moonsu.assignment.data.model.CharacterPageEntity
import com.moonsu.assignment.data.model.LocationEntity
import com.moonsu.assignment.data.model.OriginEntity
import com.moonsu.assignment.data.model.PageInfoEntity
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val info: PageInfo,
    val results: List<CharacterDto>,
) : RemoteMapper<CharacterPageEntity> {
    override fun toData() = CharacterPageEntity(
        info = info.toData(),
        results = results.map { it.toData() },
    )
}

@Serializable
data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
) : RemoteMapper<PageInfoEntity> {
    override fun toData() = PageInfoEntity(
        count = count,
        pages = pages,
        next = next,
        prev = prev,
    )
}

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginDto,
    val location: LocationDto,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
) : RemoteMapper<CharacterEntity> {
    override fun toData() = CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin.toData(),
        location = location.toData(),
        image = image,
        episode = episode,
        url = url,
        created = created,
    )
}

@Serializable
data class OriginDto(
    val name: String,
    val url: String,
) : RemoteMapper<OriginEntity> {
    override fun toData() = OriginEntity(
        name = name,
        url = url,
    )
}

@Serializable
data class LocationDto(
    val name: String,
    val url: String,
) : RemoteMapper<LocationEntity> {
    override fun toData() = LocationEntity(
        name = name,
        url = url,
    )
}
