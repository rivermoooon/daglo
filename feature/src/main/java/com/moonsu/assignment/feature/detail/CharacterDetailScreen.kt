@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.moonsu.assignment.feature.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonsu.assignment.core.designsystem.component.DagloImage
import com.moonsu.assignment.core.designsystem.component.DagloTopBar
import com.moonsu.assignment.core.designsystem.component.TopAppBarNavigationType
import com.moonsu.assignment.core.designsystem.foundation.DagloColor
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import com.moonsu.assignment.feature.model.CharacterDetailUiModel

@Composable
fun CharacterDetailRoute(
    characterId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    snackbarHostState: SnackbarHostState,
    viewModel: CharacterDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is CharacterDetailEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.error.getUserMessage())
                }
            }
        }
    }

    CharacterDetailScreen(
        state = state,
        characterId = characterId,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onBackClick = { viewModel.onIntent(CharacterDetailIntent.OnBackClick) },
        onRefresh = { viewModel.onIntent(CharacterDetailIntent.Refresh) },
    )
}

@Composable
private fun CharacterDetailScreen(
    state: CharacterDetailUiState,
    characterId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DagloTheme.colors.background),
    ) {
        CharacterDetailTopBar(onBackClick = onBackClick)

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.showErrorState -> ErrorContent(
                    message = state.error ?: "오류가 발생했습니다.",
                    onRetry = onRefresh,
                )

                else -> CharacterDetailContent(
                    character = state.character,
                    initialImageUrl = state.initialImageUrl,
                    characterId = characterId,
                    scrollState = scrollState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                )
            }
        }
    }
}

@Composable
private fun CharacterDetailTopBar(
    onBackClick: () -> Unit,
) {
    DagloTopBar(
        title = "캐릭터 상세",
        navigationType = TopAppBarNavigationType.Back,
        navigationIconContentDescription = "뒤로가기",
        onNavigationClick = onBackClick,
    )
}

@Composable
private fun CharacterDetailContent(
    character: CharacterDetailUiModel?,
    initialImageUrl: String,
    characterId: Int,
    scrollState: ScrollState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        CharacterImage(
            imageUrl = character?.imageUrl ?: initialImageUrl,
            characterId = characterId,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
        )

        if (character != null) {
            CharacterInfo(character = character)
        } else {
            CharacterInfoSkeleton()
        }
    }
}

@Composable
private fun CharacterImage(
    imageUrl: String,
    characterId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    DagloImage(
        model = imageUrl,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        sharedTransitionScope = sharedTransitionScope,
        sharedTransitionKey = "character-$characterId",
        animatedContentScope = animatedContentScope,
    )
}

@Composable
private fun CharacterInfo(character: CharacterDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CharacterHeader(
            name = character.name,
            status = character.status,
        )

        HorizontalDivider(color = DagloTheme.colors.outlineVariant)

        BasicInfoSection(character = character)

        LocationInfoSection(character = character)

        if (character.episodeCount > 0) {
            EpisodeInfoSection(episodeCount = character.episodeCount)
        }
    }
}

@Composable
private fun CharacterInfoSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CharacterHeader(
            name = "",
            status = "unknown",
        )
    }
}

@Composable
private fun CharacterHeader(
    name: String,
    status: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            style = DagloTheme.typography.headlineMediumB,
            color = DagloTheme.colors.onBackground,
            modifier = Modifier.weight(1f),
        )

        StatusBadge(status = status)
    }
}

@Composable
private fun StatusBadge(status: String) {
    val statusColors = rememberStatusColors(status)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(statusColors.background)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(statusColors.indicator),
        )
        Text(
            text = status,
            style = DagloTheme.typography.labelMediumR,
            color = statusColors.text,
        )
    }
}

@Composable
private fun rememberStatusColors(status: String): StatusColors {
    return when (status.lowercase()) {
        "alive" -> StatusColors(
            background = DagloColor.Green100,
            text = DagloColor.Green700,
            indicator = DagloColor.Green500,
        )

        "dead" -> StatusColors(
            background = DagloColor.Red100,
            text = DagloColor.Red700,
            indicator = DagloColor.Red500,
        )

        else -> StatusColors(
            background = DagloColor.Gray200,
            text = DagloColor.Gray700,
            indicator = DagloColor.Gray500,
        )
    }
}

private data class StatusColors(
    val background: Color,
    val text: Color,
    val indicator: Color,
)

@Composable
private fun BasicInfoSection(character: CharacterDetailUiModel) {
    InfoSection(title = "기본 정보") {
        InfoRow(label = "Gender", value = character.gender)
        InfoRow(label = "Species", value = character.species)
        if (character.type.isNotBlank()) {
            InfoRow(label = "Type", value = character.type)
        }
    }
}

@Composable
private fun LocationInfoSection(character: CharacterDetailUiModel) {
    InfoSection(title = "위치 정보") {
        InfoRow(label = "Origin", value = character.originName)
        InfoRow(label = "Location", value = character.locationName)
    }
}

@Composable
private fun EpisodeInfoSection(episodeCount: Int) {
    InfoSection(title = "출연 정보") {
        InfoRow(label = "Episodes", value = "${episodeCount}개 에피소드 출연")
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            style = DagloTheme.typography.titleSmallB,
            color = DagloTheme.colors.onBackground,
        )
        content()
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = DagloTheme.typography.bodyMediumR,
            color = DagloTheme.colors.onSurfaceVariant,
        )
        Text(
            text = value,
            style = DagloTheme.typography.bodyMediumR,
            color = DagloTheme.colors.onBackground,
        )
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
