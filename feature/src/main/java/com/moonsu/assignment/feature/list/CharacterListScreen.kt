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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.moonsu.assignment.core.common.gridTwoColumns
import com.moonsu.assignment.core.designsystem.component.DagloProgress
import com.moonsu.assignment.core.designsystem.component.DagloTopBar
import com.moonsu.assignment.core.designsystem.component.TopAppBarNavigationType
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import com.moonsu.assignment.domain.model.Character
import com.moonsu.assignment.feature.list.component.DagloImageCard

@Composable
fun CharacterListRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: CharacterListViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.pagedCharacters.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterListScreen(
        pagingItems = pagingItems,
        state = state,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onRefresh = { pagingItems.refresh() },
        onCharacterClick = { id -> viewModel.onIntent(CharacterListIntent.OnCharacterClick(id)) },
        onSearchClick = { viewModel.onIntent(CharacterListIntent.OnSearchClick) },
    )
}

@Composable
private fun CharacterListScreen(
    pagingItems: LazyPagingItems<Character>,
    state: CharacterListUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onRefresh: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
) {
    val isLoading = pagingItems.loadState.refresh is LoadState.Loading
    val isError = pagingItems.loadState.refresh is LoadState.Error
    val isEmpty = pagingItems.itemCount == 0 && !isLoading && !isError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DagloTheme.colors.background),
    ) {
        CharacterListTopBar(onSearchClick = onSearchClick)

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading && pagingItems.itemCount == 0 -> InitialLoadingContent()
                isError -> ErrorContent(
                    message = state.error ?: "오류가 발생했습니다.",
                    onRetry = onRefresh,
                )
                isEmpty -> EmptyContent()
                else -> CharacterListWithRefresh(
                    pagingItems = pagingItems,
                    isRefreshing = isLoading && pagingItems.itemCount > 0,
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
    pagingItems: LazyPagingItems<Character>,
    isRefreshing: Boolean,
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
            pagingItems = pagingItems,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
            onCharacterClick = onCharacterClick,
        )
    }
}

@Composable
private fun CharacterListContent(
    pagingItems: LazyPagingItems<Character>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCharacterClick: (Int) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        gridTwoColumns(
            itemCount = pagingItems.itemCount,
            key = { pagingItems[it]?.id ?: it },
        ) { index ->
            val character = pagingItems[index]
            if (character != null) {
                CharacterGridItem(
                    character = character,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    onCharacterClick = onCharacterClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        when (val appendState = pagingItems.loadState.append) {
            is LoadState.Loading -> {
                item {
                    LoadingMoreIndicator()
                }
            }
            is LoadState.Error -> {
                item {
                    ErrorItem(
                        message = appendState.error.message ?: "오류가 발생했습니다.",
                        onRetry = { pagingItems.retry() },
                    )
                }
            }
            else -> {}
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

@Composable
private fun ErrorItem(
    message: String,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
