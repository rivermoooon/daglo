package com.moonsu.assignment.feature.list

import androidx.lifecycle.viewModelScope
import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.DataResource
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.domain.model.Location
import com.moonsu.assignment.domain.model.Origin
import com.moonsu.assignment.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val navigationHelper: NavigationHelper,
    private val characterRepository: CharacterRepository,
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

    private fun loadCharacters() {
        setState { copy(isLoading = true, error = null) }

        viewModelScope.launch {
            characterRepository.getCharacters(page = 1)
                .catch { e ->
                    setState {
                        copy(
                            isLoading = false,
                            error = e.message ?: "오류가 발생했습니다.",
                        )
                    }
                }
                .collect { resource ->
                    when (resource) {
                        is DataResource.Loading -> {
                            setState { copy(isLoading = true, error = null) }
                        }
                        is DataResource.Success -> {
                            setState {
                                copy(
                                    isLoading = false,
                                    characters = resource.data,
                                    currentPage = 1,
                                    hasMorePages = true, // TODO: 페이징 정보 연동
                                    error = null,
                                )
                            }
                        }
                        is DataResource.Error -> {
                            setState {
                                copy(
                                    isLoading = false,
                                    error = resource.throwable.message ?: "오류가 발생했습니다.",
                                )
                            }
                        }
                    }
                }
        }
    }

    private suspend fun loadMoreCharacters() {
        val currentState = state.value
        if (currentState.isLoadingMore || !currentState.hasMorePages) return

        setState { copy(isLoadingMore = true) }

        val nextPage = currentState.currentPage + 1

        // TODO: 페이징 구현
        // viewModelScope.launch {
        //     characterRepository.getCharacters(page = nextPage)
        //         .collect { resource ->
        //             when (resource) {
        //                 is DataResource.Success -> {
        //                     setState {
        //                         copy(
        //                             isLoadingMore = false,
        //                             characters = characters + resource.data,
        //                             currentPage = nextPage,
        //                             hasMorePages = true, // TODO: 페이징 정보 연동
        //                         )
        //                     }
        //                 }
        //                 is DataResource.Error -> {
        //                     setState { copy(isLoadingMore = false) }
        //                 }
        //                 else -> {}
        //             }
        //         }
        //     }
        // }

        // 임시로 더미 데이터 사용
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
