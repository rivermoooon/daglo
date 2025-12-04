package com.moonsu.assignment.feature.model

import com.moonsu.assignment.domain.model.Character

data class CharacterDetailUiModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val originUrl: String,
    val locationName: String,
    val locationUrl: String,
    val episodeCount: Int,
    val created: String,
)

fun Character.toDetailUiModel(): CharacterDetailUiModel {
    return CharacterDetailUiModel(
        id = id,
        name = name,
        imageUrl = image,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = origin.name,
        originUrl = origin.url,
        locationName = location.name,
        locationUrl = location.url,
        episodeCount = episode.size,
        created = created,
    )
}
