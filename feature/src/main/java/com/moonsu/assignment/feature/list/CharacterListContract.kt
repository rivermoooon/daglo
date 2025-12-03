package com.moonsu.assignment.feature.list

import com.moonsu.assignment.core.common.base.SideEffect
import com.moonsu.assignment.core.common.base.UiIntent
import com.moonsu.assignment.core.common.base.UiState
import com.moonsu.assignment.domain.Character

data class CharacterListUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val characters: List<Character> = emptyList(),
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val error: String? = null,
) : UiState {
    val isInitialLoading: Boolean
        get() = isLoading && characters.isEmpty()

    val showEmptyState: Boolean
        get() = !isLoading && characters.isEmpty() && error == null

    val showErrorState: Boolean
        get() = !isLoading && characters.isEmpty() && error != null
}

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
