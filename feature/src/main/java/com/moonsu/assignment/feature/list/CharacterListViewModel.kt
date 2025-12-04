package com.moonsu.assignment.feature.list

import androidx.paging.PagingData
import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val navigationHelper: NavigationHelper,
    private val characterRepository: CharacterRepository,
) : BaseViewModel<CharacterListUiState, CharacterListIntent, CharacterListEffect>(
    initialState = CharacterListUiState(),
) {
    val pagedCharacters: Flow<PagingData<Character>> = characterRepository.getPagedCharacters()

    override suspend fun processIntent(intent: CharacterListIntent) {
        when (intent) {
            is CharacterListIntent.Refresh -> refresh()
            is CharacterListIntent.OnCharacterClick -> navigateToDetail(intent.characterId)
            is CharacterListIntent.OnSearchClick -> navigateToSearch()
            is CharacterListIntent.LoadCharacters -> {
                // PagingData는 자동으로 로드되므로 아무 작업도 하지 않음
            }
            is CharacterListIntent.LoadMoreCharacters -> {
                // PagingData는 자동으로 로드되므로 아무 작업도 하지 않음
            }
        }
    }

    private fun refresh() {
        // PagingData의 refresh는 UI에서 collectAsLazyPagingItems().refresh()로 처리
    }

    private fun navigateToDetail(characterId: Int) {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterDetail(characterId)))
    }

    private fun navigateToSearch() {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterSearch))
    }
}
