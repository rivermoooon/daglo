package com.moonsu.assignment.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.domain.model.Location
import com.moonsu.assignment.domain.model.Origin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navigationHelper: NavigationHelper,
    // TODO: UseCase 주입
    // private val getCharacterUseCase: GetCharacterUseCase,
) : BaseViewModel<CharacterDetailUiState, CharacterDetailIntent, CharacterDetailEffect>(
    initialState = CharacterDetailUiState(),
) {
    private val characterId: Int =
        savedStateHandle.toRoute<DagloRoute.CharacterDetail>().characterId

    init {
        onIntent(CharacterDetailIntent.LoadCharacter)
    }

    override suspend fun processIntent(intent: CharacterDetailIntent) {
        when (intent) {
            is CharacterDetailIntent.LoadCharacter -> loadCharacter()
            is CharacterDetailIntent.Refresh -> refresh()
            is CharacterDetailIntent.OnBackClick -> navigateBack()
        }
    }

    private suspend fun loadCharacter() {
        setState { copy(isLoading = true, error = null) }

        // TODO: UseCase 연동
        // runCatching { getCharacterUseCase(characterId) }
        //     .onSuccess { character -> setState { copy(isLoading = false, character = character) } }
        //     .onFailure { e -> setState { copy(isLoading = false, error = e.message) } }

        // 더미 데이터로 UI 확인
        delay(100)
        setState {
            copy(
                isLoading = false,
                character = createDummyCharacter(characterId),
            )
        }
    }

    private suspend fun refresh() {
        setState { copy(error = null) }
        loadCharacter()
    }

    private fun navigateBack() {
        navigationHelper.navigate(NavigationEvent.Up())
    }

    // TODO: 서버 연동 시 삭제
    private fun createDummyCharacter(id: Int): Character {
        val statuses = listOf("Alive", "Dead", "unknown")
        val genders = listOf("Male", "Female", "unknown")
        val species = listOf("Human", "Alien", "Humanoid", "Robot")

        return Character(
            id = id,
            name = "Rick Sanchez",
            status = statuses[id % 3],
            species = species[id % 4],
            type = if (id % 2 == 0) "Genetic experiment" else "",
            gender = genders[id % 3],
            origin = Origin(
                name = "Earth (C-137)",
                url = "https://rickandmortyapi.com/api/location/1",
            ),
            location = Location(
                name = "Citadel of Ricks",
                url = "https://rickandmortyapi.com/api/location/3",
            ),
            image = "https://rickandmortyapi.com/api/character/avatar/${(id % 826) + 1}.jpeg",
            episode = listOf(
                "https://rickandmortyapi.com/api/episode/1",
                "https://rickandmortyapi.com/api/episode/2",
                "https://rickandmortyapi.com/api/episode/3",
            ),
            url = "https://rickandmortyapi.com/api/character/$id",
            created = "2017-11-04T18:48:46.250Z",
        )
    }
}
