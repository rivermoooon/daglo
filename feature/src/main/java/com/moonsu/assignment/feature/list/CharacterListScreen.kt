@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)

package com.moonsu.assignment.feature.list

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonsu.assignment.core.common.gridTwoColumns
import com.moonsu.assignment.core.designsystem.component.DagloProgress
import com.moonsu.assignment.core.designsystem.component.DagloTopBar
import com.moonsu.assignment.core.designsystem.component.TopAppBarNavigationType
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import com.moonsu.assignment.domain.Character
import com.moonsu.assignment.feature.list.component.DagloImageCard
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun CharacterListRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: CharacterListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterListScreen(
        state = state,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onRefresh = { viewModel.onIntent(CharacterListIntent.Refresh) },
        onCharacterClick = { id -> viewModel.onIntent(CharacterListIntent.OnCharacterClick(id)) },
        onSearchClick = { viewModel.onIntent(CharacterListIntent.OnSearchClick) },
        onLoadMore = { viewModel.onIntent(CharacterListIntent.LoadMoreCharacters) },
    )
}

@Composable
private fun CharacterListScreen(
    state: CharacterListUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onRefresh: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = layoutInfo.totalItemsCount

            totalItems > 0 && lastVisibleItem >= totalItems - 3
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { shouldLoadMore }
            .distinctUntilChanged()
            .filter {
                it &&
                    !state.isLoadingMore &&
                    state.hasMorePages &&
                    state.characters.isNotEmpty()
            }
            .collect { onLoadMore() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DagloTheme.colors.background),
    ) {
        CharacterListTopBar(onSearchClick = onSearchClick)

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isInitialLoading -> InitialLoadingContent()
                state.showErrorState -> ErrorContent(
                    message = state.error ?: "오류가 발생했습니다.",
                    onRetry = onRefresh,
                )

                state.showEmptyState -> EmptyContent()
                else -> CharacterListWithRefresh(
                    characters = state.characters,
                    isRefreshing = state.isLoading && state.characters.isNotEmpty(),
                    isLoadingMore = state.isLoadingMore,
                    listState = listState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onRefresh = onRefresh,
                    onCharacterClick = onCharacterClick,
                )
            }
        }
    }
}

@Composable
private fun CharacterListTopBar(
    onSearchClick: () -> Unit,
) {
    DagloTopBar(
        title = "캐릭터 목록",
        navigationType = TopAppBarNavigationType.None,
        actionButtons = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = DagloTheme.colors.onSurface,
                )
            }
        },
    )
}

@Composable
private fun CharacterListWithRefresh(
    characters: List<Character>,
    isRefreshing: Boolean,
    isLoadingMore: Boolean,
    listState: LazyListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onRefresh: () -> Unit,
    onCharacterClick: (Int) -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize(),
    ) {
        CharacterListContent(
            characters = characters,
            isLoadingMore = isLoadingMore,
            listState = listState,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
            onCharacterClick = onCharacterClick,
        )
    }
}

@Composable
private fun CharacterListContent(
    characters: List<Character>,
    isLoadingMore: Boolean,
    listState: LazyListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCharacterClick: (Int) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        gridTwoColumns(
            itemCount = characters.size,
            key = { characters[it].id },
        ) { index ->
            CharacterGridItem(
                character = characters[index],
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope,
                onCharacterClick = onCharacterClick,
                modifier = Modifier.weight(1f),
            )
        }

        if (isLoadingMore) {
            item {
                LoadingMoreIndicator()
            }
        }
    }
}

@Composable
private fun CharacterGridItem(
    character: Character,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    DagloImageCard(
        imageUrl = character.image,
        name = character.name,
        status = character.status,
        gender = character.gender,
        onClick = { onCharacterClick(character.id) },
        sharedTransitionScope = sharedTransitionScope,
        sharedTransitionKey = "character-${character.id}",
        animatedContentScope = animatedContentScope,
        modifier = modifier,
    )
}

@Composable
private fun LoadingMoreIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        DagloProgress()
    }
}

@Composable
private fun InitialLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        DagloProgress()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = message,
                style = DagloTheme.typography.bodyMediumR,
                color = DagloTheme.colors.onSurfaceVariant,
            )
            TextButton(onClick = onRetry) {
                Text(
                    text = "다시 시도",
                    style = DagloTheme.typography.titleSmallB,
                    color = DagloTheme.colors.primary,
                )
            }
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "캐릭터가 없습니다.",
            style = DagloTheme.typography.bodyMediumR,
            color = DagloTheme.colors.onSurfaceVariant,
        )
    }
}
