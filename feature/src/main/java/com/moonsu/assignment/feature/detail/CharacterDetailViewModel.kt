package com.moonsu.assignment.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.moonsu.assignment.core.common.AppError
import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.DataResource
import com.moonsu.assignment.domain.repository.CharacterRepository
import com.moonsu.assignment.feature.model.toDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navigationHelper: NavigationHelper,
    private val characterRepository: CharacterRepository,
) : BaseViewModel<CharacterDetailUiState, CharacterDetailIntent, CharacterDetailEffect>(
    initialState = CharacterDetailUiState(),
) {
    private val characterId: Int =
        savedStateHandle.toRoute<DagloRoute.CharacterDetail>().characterId

    init {
        val initialImageUrl = savedStateHandle.toRoute<DagloRoute.CharacterDetail>().imageUrl
        setState { copy(initialImageUrl = initialImageUrl) }
        onIntent(CharacterDetailIntent.LoadCharacter)
    }

    override suspend fun processIntent(intent: CharacterDetailIntent) {
        when (intent) {
            is CharacterDetailIntent.LoadCharacter -> loadCharacter()
            is CharacterDetailIntent.Refresh -> refresh()
            is CharacterDetailIntent.OnBackClick -> navigateBack()
        }
    }

    private fun loadCharacter() {
        setState { copy(isLoading = true, error = null) }

        viewModelScope.launch {
            characterRepository.getCharacter(characterId)
                .collect { resource ->
                    when (resource) {
                        is DataResource.Loading -> {
                            setState { copy(isLoading = true, error = null) }
                        }

                        is DataResource.Success -> {
                            val uiModel = resource.data.toDetailUiModel()
                            setState {
                                copy(
                                    isLoading = false,
                                    character = uiModel,
                                    error = null,
                                )
                            }
                        }

                        is DataResource.Error -> {
                            setState {
                                copy(
                                    isLoading = false,
                                    error = resource.error.getUserMessage(),
                                )
                            }
                            if (resource.error is AppError.NetworkError ||
                                resource.error is AppError.TimeoutError
                            ) {
                                postSideEffect(CharacterDetailEffect.ShowError(resource.error))
                            }
                        }
                    }
                }
        }
    }

    private suspend fun refresh() {
        setState { copy(error = null) }
        loadCharacter()
    }

    private fun navigateBack() {
        navigationHelper.navigate(NavigationEvent.Up())
    }
}
