@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.moonsu.assignment.feature.search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonsu.assignment.core.common.gridTwoColumns
import com.moonsu.assignment.core.designsystem.component.DagloProgress
import com.moonsu.assignment.core.designsystem.component.DagloTextField
import com.moonsu.assignment.core.designsystem.component.DagloTopBar
import com.moonsu.assignment.core.designsystem.component.TopAppBarNavigationType
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import com.moonsu.assignment.feature.list.component.DagloImageCard
import com.moonsu.assignment.feature.model.CharacterSearchItem

@Composable
internal fun CharacterSearchRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: CharacterSearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterSearchScreen(
        state = state,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onQueryChange = { query -> viewModel.onIntent(CharacterSearchIntent.OnQueryChange(query)) },
        onCharacterClick = { id, imageUrl ->
            viewModel.onIntent(CharacterSearchIntent.OnCharacterClick(id, imageUrl))
        },
        onBackClick = { viewModel.onIntent(CharacterSearchIntent.OnBackClick) },
    )
}

@Composable
private fun CharacterSearchScreen(
    state: CharacterSearchUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onQueryChange: (String) -> Unit,
    onCharacterClick: (Int, String) -> Unit,
    onBackClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DagloTheme.colors.background),
    ) {
        CharacterSearchTopBar(onBackClick = onBackClick)

        SearchTextField(
            query = state.query,
            onQueryChange = onQueryChange,
        )

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isSearching -> SearchingContent()
                state.showInitialState -> InitialContent()
                state.showEmptyResult -> EmptyResultContent()
                state.showResults -> SearchResultsContent(
                    searchResults = state.searchResults,
                    listState = listState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onCharacterClick = onCharacterClick,
                )

                state.error != null -> ErrorContent(message = state.error)
            }
        }
    }
}

@Composable
private fun CharacterSearchTopBar(
    onBackClick: () -> Unit,
) {
    DagloTopBar(
        title = "캐릭터 검색",
        navigationType = TopAppBarNavigationType.Back,
        navigationIconContentDescription = "뒤로가기",
        onNavigationClick = onBackClick,
    )
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    DagloTextField(
        value = query,
        onValueChange = onQueryChange,
        hint = "캐릭터 이름을 입력하세요",
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun SearchResultsContent(
    searchResults: List<CharacterSearchItem>,
    listState: LazyListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCharacterClick: (Int, String) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        gridTwoColumns(
            itemCount = searchResults.size,
            key = { searchResults[it].id },
        ) { index ->
            SearchResultItem(
                item = searchResults[index],
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onCharacterClick = onCharacterClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    item: CharacterSearchItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCharacterClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DagloImageCard(
        imageUrl = item.imageUrl,
        name = item.name,
        status = item.status,
        gender = item.gender,
        onClick = { onCharacterClick(item.id, item.imageUrl) },
        sharedTransitionScope = sharedTransitionScope,
        sharedTransitionKey = "character-${item.id}",
        animatedContentScope = animatedContentScope,
        modifier = modifier,
    )
}

@Composable
private fun SearchingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        DagloProgress()
    }
}

@Composable
private fun InitialContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "캐릭터 이름으로 검색해보세요",
            style = DagloTheme.typography.bodyMediumR,
            color = DagloTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun EmptyResultContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "검색 결과가 없습니다",
            style = DagloTheme.typography.bodyMediumR,
            color = DagloTheme.colors.onSurfaceVariant,
        )
    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = DagloTheme.typography.bodyMediumR,
            color = DagloTheme.colors.error,
        )
    }
}
