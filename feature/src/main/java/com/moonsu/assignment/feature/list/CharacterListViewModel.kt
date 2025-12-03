package com.moonsu.assignment.feature.list

import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.Character
import com.moonsu.assignment.domain.Location
import com.moonsu.assignment.domain.Origin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val navigationHelper: NavigationHelper,
    // TODO: UseCase 주입
    // private val getCharactersUseCase: GetCharactersUseCase,
) : BaseViewModel<CharacterListUiState, CharacterListIntent, CharacterListEffect>(
    initialState = CharacterListUiState(),
) {
    init {
        onIntent(CharacterListIntent.LoadCharacters)
    }

    override suspend fun processIntent(intent: CharacterListIntent) {
        when (intent) {
            is CharacterListIntent.LoadCharacters -> loadCharacters()
            is CharacterListIntent.LoadMoreCharacters -> loadMoreCharacters()
            is CharacterListIntent.Refresh -> refresh()
            is CharacterListIntent.OnCharacterClick -> navigateToDetail(intent.characterId)
            is CharacterListIntent.OnSearchClick -> navigateToSearch()
        }
    }

    private suspend fun loadCharacters() {
        setState { copy(isLoading = true, error = null) }

        // TODO: UseCase 연동
        // runCatching { getCharactersUseCase(page = 1) }
        //     .onSuccess { characters -> setState { copy(isLoading = false, characters = characters) } }
        //     .onFailure { e -> setState { copy(isLoading = false, error = e.message) } }

        // 더미 데이터로 UI 확인
        delay(1000)
        setState {
            copy(
                isLoading = false,
                characters = createDummyCharacters(),
                currentPage = 1,
                hasMorePages = true,
            )
        }
    }

    private suspend fun loadMoreCharacters() {
        val currentState = state.value
        if (currentState.isLoadingMore || !currentState.hasMorePages) return

        setState { copy(isLoadingMore = true) }

        val nextPage = currentState.currentPage + 1

        // TODO: UseCase 연동
        // runCatching { getCharactersUseCase(page = nextPage) }
        //     .onSuccess { newCharacters -> ... }
        //     .onFailure { ... }

        // 더미 데이터로 페이지네이션 확인
        delay(1000)
        setState {
            copy(
                isLoadingMore = false,
                characters = characters + createDummyCharacters(startId = nextPage * 10),
                currentPage = nextPage,
                hasMorePages = nextPage < 5,
            )
        }
    }

    private suspend fun refresh() {
        setState { copy(error = null) }
        loadCharacters()
    }

    private fun navigateToDetail(characterId: Int) {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterDetail(characterId)))
    }

    private fun navigateToSearch() {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterSearch))
    }

    // TODO: 서버 연동 시 삭제
    private fun createDummyCharacters(startId: Int = 1): List<Character> {
        val statuses = listOf("Alive", "Dead", "unknown")
        val genders = listOf("Male", "Female", "unknown")

        return (startId until startId + 10).map { id ->
            Character(
                id = id,
                name = "Character $id",
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
