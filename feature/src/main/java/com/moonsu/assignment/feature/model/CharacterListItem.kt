package com.moonsu.assignment.feature.model

import com.moonsu.assignment.domain.model.Character

data class CharacterListItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val status: String,
    val gender: String,
)
fun Character.toListItem(): CharacterListItem {
    return CharacterListItem(
        id = id,
        name = name,
        imageUrl = image,
        status = status,
        gender = gender,
    )
}
