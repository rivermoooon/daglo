package com.moonsu.assignment.feature.search

import androidx.lifecycle.viewModelScope
import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.DataResource
import com.moonsu.assignment.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@OptIn(FlowPreview::class)
@HiltViewModel
class CharacterSearchViewModel @Inject constructor(
    private val navigationHelper: NavigationHelper,
    private val characterRepository: CharacterRepository,
) : BaseViewModel<CharacterSearchUiState, CharacterSearchIntent, CharacterSearchEffect>(
    initialState = CharacterSearchUiState(),
) {
    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeSearchQuery()
    }

    override suspend fun processIntent(intent: CharacterSearchIntent) {
        when (intent) {
            is CharacterSearchIntent.OnQueryChange -> handleQueryChange(intent.query)
            is CharacterSearchIntent.OnCharacterClick -> navigateToDetail(intent.characterId)
            is CharacterSearchIntent.OnBackClick -> navigateBack()
        }
    }

    private fun handleQueryChange(newQuery: String) {
        if (newQuery.isNotEmpty()) {
            setState { copy(query = newQuery, isSearching = true, error = null) }
        } else {
            setState {
                copy(
                    query = newQuery,
                    isSearching = false,
                    searchResults = emptyList(),
                    error = null,
                )
            }
        }
        searchQueryFlow.value = newQuery
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isNotEmpty()) {
                        characterRepository.searchCharacters(query)
                    } else {
                        flowOf(DataResource.Success(emptyList()))
                    }
                }
                .catch { e ->
                    setState {
                        copy(
                            isSearching = false,
                            error = e.message ?: "검색 중 오류가 발생했습니다.",
                        )
                    }
                }
                .collect { resource ->
                    when (resource) {
                        is DataResource.Loading -> {
                            setState { copy(isSearching = true, error = null) }
                        }
                        is DataResource.Success -> {
                            setState {
                                copy(
                                    isSearching = false,
                                    searchResults = resource.data,
                                    error = null,
                                )
                            }
                        }
                        is DataResource.Error -> {
                            setState {
                                copy(
                                    isSearching = false,
                                    error = resource.throwable.message ?: "검색 중 오류가 발생했습니다.",
                                )
                            }
                            postSideEffect(
                                CharacterSearchEffect.ShowError(
                                    resource.throwable.message ?: "검색 실패",
                                ),
                            )
                        }
                    }
                }
        }
    }

    private fun navigateToDetail(characterId: Int) {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterDetail(characterId)))
    }

    private fun navigateBack() {
        navigationHelper.navigate(NavigationEvent.Up())
    }
}
