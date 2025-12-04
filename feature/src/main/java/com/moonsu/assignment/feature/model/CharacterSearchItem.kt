package com.moonsu.assignment.feature.model

import com.moonsu.assignment.domain.model.Character

data class CharacterSearchItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: String,
    val gender: String,
)

fun Character.toSearchItem(): CharacterSearchItem {
    return CharacterSearchItem(
        id = id,
        name = name,
        imageUrl = image,
        status = status,
        gender = gender,
    )
}

fun List<Character>.toSearchItems(): List<CharacterSearchItem> {
    return map { it.toSearchItem() }
}
