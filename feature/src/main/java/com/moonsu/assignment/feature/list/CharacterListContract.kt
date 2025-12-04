package com.moonsu.assignment.feature.list

import com.moonsu.assignment.core.common.base.SideEffect
import com.moonsu.assignment.core.common.base.UiIntent
import com.moonsu.assignment.core.common.base.UiState
import com.moonsu.assignment.domain.model.Character

data class CharacterListUiState(
    val error: String? = null,
) : UiState

sealed interface CharacterListIntent : UiIntent {
    data object LoadCharacters : CharacterListIntent
    data object LoadMoreCharacters : CharacterListIntent
    data object Refresh : CharacterListIntent
    data class OnCharacterClick(val characterId: Int) : CharacterListIntent
    data object OnSearchClick : CharacterListIntent
}

sealed interface CharacterListEffect : SideEffect {
    data class ShowError(val message: String) : CharacterListEffect
}
