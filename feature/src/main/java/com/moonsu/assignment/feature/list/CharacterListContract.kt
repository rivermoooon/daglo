package com.moonsu.assignment.feature.list

import com.moonsu.assignment.core.common.base.SideEffect
import com.moonsu.assignment.core.common.base.UiIntent
import com.moonsu.assignment.core.common.base.UiState

data class CharacterListUiState(
    val error: String? = null,
) : UiState

sealed interface CharacterListIntent : UiIntent {
    data class OnCharacterClick(val characterId: Int, val imageUrl: String) : CharacterListIntent
    data object OnSearchClick : CharacterListIntent
}

sealed interface CharacterListEffect : SideEffect {
    data class ShowError(val message: String) : CharacterListEffect
}
