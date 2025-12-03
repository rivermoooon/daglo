package com.moonsu.assignment.feature.detail

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonsu.assignment.core.designsystem.component.DagloImage
import com.moonsu.assignment.core.designsystem.component.DagloProgress
import com.moonsu.assignment.core.designsystem.component.DagloTopBar
import com.moonsu.assignment.core.designsystem.component.TopAppBarNavigationType
import com.moonsu.assignment.core.designsystem.foundation.DagloColor
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import com.moonsu.assignment.domain.Character

@Composable
internal fun CharacterDetailRoute(
    characterId: Int,
    viewModel: CharacterDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CharacterDetailScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun CharacterDetailScreen(
    state: CharacterDetailUiState,
    onIntent: (CharacterDetailIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DagloTheme.colors.background),
    ) {
        DagloTopBar(
            title = "캐릭터 상세",
            navigationType = TopAppBarNavigationType.Back,
            navigationIconContentDescription = "뒤로가기",
            onNavigationClick = { onIntent(CharacterDetailIntent.OnBackClick) },
        )

        Box(modifier = Modifier.weight(1f)) {
            when {
                state.showLoadingState -> {
                    LoadingContent()
                }

                state.showErrorState -> {
                    ErrorContent(
                        message = state.error ?: "오류가 발생했습니다.",
                        onRetry = { onIntent(CharacterDetailIntent.Refresh) },
                    )
                }

                state.showContent -> {
                    state.character?.let { character ->
                        CharacterDetailContent(character = character)
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(
    character: Character,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        DagloImage(
            model = character.image,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        )

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

            InfoSection(title = "기본 정보") {
                InfoRow(label = "Gender", value = character.gender)
                InfoRow(label = "Species", value = character.species)
                if (character.type.isNotBlank()) {
                    InfoRow(label = "Type", value = character.type)
                }
            }

            InfoSection(title = "위치 정보") {
                InfoRow(label = "Origin", value = character.origin.name)
                InfoRow(label = "Location", value = character.location.name)
            }

            if (character.episode.isNotEmpty()) {
                InfoSection(title = "출연 정보") {
                    InfoRow(label = "Episodes", value = "${character.episode.size}개 에피소드 출연")
                }
            }
        }
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
    val (backgroundColor, textColor, indicatorColor) = when (status.lowercase()) {
        "alive" -> Triple(
            DagloColor.Green100,
            DagloColor.Green700,
            DagloColor.Green500,
        )

        "dead" -> Triple(
            DagloColor.Red100,
            DagloColor.Red700,
            DagloColor.Red500,
        )

        else -> Triple(
            DagloColor.Gray200,
            DagloColor.Gray700,
            DagloColor.Gray500,
        )
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(indicatorColor),
        )
        Text(
            text = status,
            style = DagloTheme.typography.labelMediumR,
            color = textColor,
        )
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
private fun LoadingContent() {
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
