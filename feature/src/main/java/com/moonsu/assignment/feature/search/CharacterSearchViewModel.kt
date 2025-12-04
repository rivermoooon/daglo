package com.moonsu.assignment.feature.search

import androidx.lifecycle.viewModelScope
import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.domain.model.Location
import com.moonsu.assignment.domain.model.Origin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharacterSearchViewModel @Inject constructor(
    private val navigationHelper: NavigationHelper,
    // TODO: UseCase 주입
    // private val searchCharactersUseCase: SearchCharactersUseCase,
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
                .collect { query ->
                    if (query.isNotEmpty()) {
                        searchCharacters(query)
                    }
                }
        }
    }

    private suspend fun searchCharacters(query: String) {
        // TODO: UseCase 연동
        // runCatching { searchCharactersUseCase(query) }
        //     .onSuccess { results -> setState { copy(isSearching = false, searchResults = results) } }
        //     .onFailure { e -> setState { copy(isSearching = false, error = e.message) } }

        // 더미 데이터로 검색 결과 필터링
        delay(500)
        val allCharacters = createDummyCharacters()
        val filteredResults = allCharacters.filter {
            it.name.contains(query, ignoreCase = true)
        }
        setState {
            copy(
                isSearching = false,
                searchResults = filteredResults,
            )
        }
    }

    private fun navigateToDetail(characterId: Int) {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterDetail(characterId)))
    }

    private fun navigateBack() {
        navigationHelper.navigate(NavigationEvent.Up())
    }

    // TODO: 서버 연동 시 삭제
    private fun createDummyCharacters(): List<Character> {
        val statuses = listOf("Alive", "Dead", "unknown")
        val genders = listOf("Male", "Female", "unknown")
        val names = listOf(
            "Rick Sanchez", "Morty Smith", "Summer Smith", "Beth Smith", "Jerry Smith",
            "Abadango Cluster Princess", "Abradolf Lincler", "Adjudicator Rick",
            "Agency Director", "Alan Rails", "Albert Einstein", "Alexander",
            "Alien Googah", "Alien Morty", "Alien Rick", "Amish Cyborg",
            "Annie", "Antenna Morty", "Antenna Rick", "Ants in my Eyes Johnson",
        )

        return names.mapIndexed { index, name ->
            val id = index + 1
            Character(
                id = id,
                name = name,
                status = statuses[id % 3],
                species = "Human",
                type = "",
                gender = genders[id % 3],
                origin = Origin(
                    name = "Earth",
                    url = "",
                ),
                location = Location(
                    name = "Earth",
                    url = "",
                ),
                image = "https://rickandmortyapi.com/api/character/avatar/${(id % 826) + 1}.jpeg",
                episode = emptyList(),
                url = "",
                created = "",
            )
        }
    }
}
