package com.moonsu.assignment.feature.detail

import com.moonsu.assignment.core.common.base.SideEffect
import com.moonsu.assignment.core.common.base.UiIntent
import com.moonsu.assignment.core.common.base.UiState
import com.moonsu.assignment.feature.model.CharacterDetailUiModel

data class CharacterDetailUiState(
    val isLoading: Boolean = false,
    val character: CharacterDetailUiModel? = null,
    val initialImageUrl: String = "",
    val error: String? = null,
) : UiState {
    val showLoadingState: Boolean
        get() = isLoading && character == null

    val showErrorState: Boolean
        get() = !isLoading && character == null && error != null

    val showContent: Boolean
        get() = character != null
}

sealed interface CharacterDetailIntent : UiIntent {
    data object LoadCharacter : CharacterDetailIntent
    data object Refresh : CharacterDetailIntent
    data object OnBackClick : CharacterDetailIntent
}

sealed interface CharacterDetailEffect : SideEffect {
    data class ShowError(val message: String) : CharacterDetailEffect
}
