package com.moonsu.assignment.core.navigation

import kotlinx.serialization.Serializable

sealed interface DagloRoute {
    @Serializable
    data object CharacterList : DagloRoute

    @Serializable
    data class CharacterDetail(val characterId: Int, val imageUrl: String) : DagloRoute

    @Serializable
    data object CharacterSearch : DagloRoute
}
