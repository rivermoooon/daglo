package com.moonsu.assignment.feature.list

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.moonsu.assignment.feature.list.component.DagloImageCard

@Composable
internal fun CharacterListRoute(
    viewModel: CharacterListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterListScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListScreen(
    state: CharacterListUiState,
    onIntent: (CharacterListIntent) -> Unit,
) {
    val listState = rememberLazyListState()

    // 스크롤 위치 기반 페이지네이션 트리거
    val reachedBottom by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            totalItems > 0 && lastVisibleItem >= totalItems - 3
        }
    }

    LaunchedEffect(reachedBottom, state.isLoadingMore, state.hasMorePages) {
        if (reachedBottom && !state.isLoadingMore && state.hasMorePages && state.characters.isNotEmpty()) {
            onIntent(CharacterListIntent.LoadMoreCharacters)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DagloTheme.colors.background),
    ) {
        DagloTopBar(
            title = "캐릭터 목록",
            navigationType = TopAppBarNavigationType.None,
        )

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.isInitialLoading -> {
                    InitialLoadingContent()
                }

                state.showErrorState -> {
                    ErrorContent(
                        message = state.error ?: "오류가 발생했습니다.",
                        onRetry = { onIntent(CharacterListIntent.Refresh) },
                    )
                }

                state.showEmptyState -> {
                    EmptyContent()
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isLoading && state.characters.isNotEmpty(),
                        onRefresh = { onIntent(CharacterListIntent.Refresh) },
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        CharacterListContent(
                            state = state,
                            listState = listState,
                            onCharacterClick = { id ->
                                onIntent(CharacterListIntent.OnCharacterClick(id))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterListContent(
    state: CharacterListUiState,
    listState: LazyListState,
    onCharacterClick: (Int) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        gridTwoColumns(
            itemCount = state.characters.size,
            key = { state.characters[it].id },
        ) { index ->
            val character = state.characters[index]
            DagloImageCard(
                imageUrl = character.image,
                name = character.name,
                status = character.status,
                gender = character.gender,
                onClick = { onCharacterClick(character.id) },
                modifier = Modifier.weight(1f),
            )
        }

        if (state.isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    DagloProgress()
                }
            }
        }
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
