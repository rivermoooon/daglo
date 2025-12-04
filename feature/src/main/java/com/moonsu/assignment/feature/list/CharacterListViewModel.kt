package com.moonsu.assignment.feature.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.moonsu.assignment.core.common.base.BaseViewModel
import com.moonsu.assignment.core.navigation.DagloRoute
import com.moonsu.assignment.core.navigation.NavigationEvent
import com.moonsu.assignment.core.navigation.NavigationHelper
import com.moonsu.assignment.domain.repository.CharacterRepository
import com.moonsu.assignment.feature.model.CharacterListItem
import com.moonsu.assignment.feature.model.toListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val navigationHelper: NavigationHelper,
    private val characterRepository: CharacterRepository,
) : BaseViewModel<CharacterListUiState, CharacterListIntent, CharacterListEffect>(
    initialState = CharacterListUiState(),
) {

    val pagedCharacters: Flow<PagingData<CharacterListItem>> =
        characterRepository.getPagedCharacters()
            .map { pagingData ->
                pagingData.map { it.toListItem() }
            }
            .cachedIn(viewModelScope)

    override suspend fun processIntent(intent: CharacterListIntent) {
        when (intent) {
            is CharacterListIntent.OnCharacterClick -> navigateToDetail(intent.characterId)
            is CharacterListIntent.OnSearchClick -> navigateToSearch()
        }
    }

    private fun navigateToDetail(characterId: Int) {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterDetail(characterId)))
    }

    private fun navigateToSearch() {
        navigationHelper.navigate(NavigationEvent.To(DagloRoute.CharacterSearch))
    }
}
