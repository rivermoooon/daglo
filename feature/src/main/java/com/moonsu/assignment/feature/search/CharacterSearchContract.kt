package com.moonsu.assignment.feature.search

import com.moonsu.assignment.core.common.AppError
import com.moonsu.assignment.core.common.base.SideEffect
import com.moonsu.assignment.core.common.base.UiIntent
import com.moonsu.assignment.core.common.base.UiState
import com.moonsu.assignment.feature.model.CharacterSearchItem

data class CharacterSearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<CharacterSearchItem> = emptyList(),
    val error: String? = null,
) : UiState {
    val showInitialState: Boolean
        get() = query.isEmpty() && searchResults.isEmpty()

    val showEmptyResult: Boolean
        get() = query.isNotEmpty() && !isSearching && searchResults.isEmpty() && error == null

    val showResults: Boolean
        get() = searchResults.isNotEmpty()
}

sealed interface CharacterSearchIntent : UiIntent {
    data class OnQueryChange(val query: String) : CharacterSearchIntent
    data class OnCharacterClick(val characterId: Int, val imageUrl: String) : CharacterSearchIntent
    data object OnBackClick : CharacterSearchIntent
}

sealed interface CharacterSearchEffect : SideEffect {
    data class ShowError(val error: AppError) : CharacterSearchEffect
}
