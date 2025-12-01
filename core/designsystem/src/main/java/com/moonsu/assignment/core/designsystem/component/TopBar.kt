package com.moonsu.assignment.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.moonsu.assignment.core.designsystem.component.preview.DagloPreview
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme

enum class TopAppBarNavigationType {
    Back,
    Close,
    None,
}

@Composable
fun DagloTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationType: TopAppBarNavigationType = TopAppBarNavigationType.Back,
    navigationIconContentDescription: String? = null,
    onNavigationClick: () -> Unit = {},
    actionButtons: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(DagloTheme.colors.surface),
    ) {
        when (navigationType) {
            TopAppBarNavigationType.Back -> {
                NavigationIcon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = navigationIconContentDescription,
                    onClick = onNavigationClick,
                    modifier = Modifier.align(Alignment.CenterStart),
                )
            }

            TopAppBarNavigationType.Close -> {
            }

            TopAppBarNavigationType.None -> {
            }
        }

        val titleModifier = when (navigationType) {
            TopAppBarNavigationType.None -> {
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 16.dp)
            }

            else -> {
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 56.dp)
            }
        }

        Text(
            text = title,
            style = DagloTheme.typography.titleMediumB,
            color = DagloTheme.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = if (navigationType == TopAppBarNavigationType.None) {
                TextAlign.Start
            } else {
                TextAlign.Center
            },
            modifier = titleModifier,
        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when (navigationType) {
                TopAppBarNavigationType.Close -> {
                    actionButtons()
                    NavigationIcon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = navigationIconContentDescription,
                        onClick = onNavigationClick,
                    )
                }

                else -> {
                    actionButtons()
                }
            }
        }
    }
}

@Composable
private fun NavigationIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = DagloTheme.colors.onSurface,
        )
    }
}

@DagloPreview
@Composable
private fun PreviewDagloTopBarBack() {
    DagloTheme {
        DagloTopBar(
            title = "뒤로가기",
            navigationType = TopAppBarNavigationType.Back,
            navigationIconContentDescription = "뒤로가기",
            onNavigationClick = {},
        )
    }
}

@DagloPreview
@Composable
private fun PreviewDagloTopBarClose() {
    DagloTheme {
        DagloTopBar(
            title = "닫기",
            navigationType = TopAppBarNavigationType.Close,
            navigationIconContentDescription = "닫기",
            onNavigationClick = {},
        )
    }
}

@DagloPreview
@Composable
private fun PreviewDagloTopBarNone() {
    DagloTheme {
        DagloTopBar(
            title = "타이틀만 있는 상단바",
            navigationType = TopAppBarNavigationType.None,
        )
    }
}

@DagloPreview
@Composable
private fun PreviewDagloTopBarWithAction() {
    DagloTheme {
        DagloTopBar(
            title = "액션 버튼",
            navigationType = TopAppBarNavigationType.Back,
            navigationIconContentDescription = "뒤로가기",
            onNavigationClick = {},
            actionButtons = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "더보기",
                        tint = DagloTheme.colors.onSurface,
                    )
                }
            },
        )
    }
}
