package com.moonsu.assignment.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.moonsu.assignment.core.designsystem.component.preview.BasePreview
import com.moonsu.assignment.core.designsystem.component.preview.DagloPreview
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import kotlinx.coroutines.delay

enum class SnackBarType {
    DEFAULT,
    ERROR,
    WARNING,
}

@Composable
fun DagloSnackBar(
    snackBarData: SnackbarData,
    modifier: Modifier = Modifier,
    type: SnackBarType = SnackBarType.DEFAULT,
) {
    val backgroundColor = when (type) {
        SnackBarType.DEFAULT -> DagloTheme.colors.inverseSurface
        SnackBarType.ERROR -> DagloTheme.colors.error
        SnackBarType.WARNING -> DagloTheme.colors.secondary
    }

    val contentColor = when (type) {
        SnackBarType.DEFAULT -> DagloTheme.colors.inverseOnSurface
        SnackBarType.ERROR -> DagloTheme.colors.onError
        SnackBarType.WARNING -> DagloTheme.colors.onSecondary
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Text(
            text = snackBarData.visuals.message,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = DagloTheme.typography.bodyMediumR,
            color = contentColor,
        )
    }
}

@Composable
fun DagloSnackBarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    type: SnackBarType = SnackBarType.DEFAULT,
    duration: Long = 2000L,
) {
    val currentSnackbarData = hostState.currentSnackbarData

    LaunchedEffect(currentSnackbarData) {
        currentSnackbarData?.let {
            delay(duration)
            it.dismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedVisibility(
            visible = currentSnackbarData != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        ) {
            currentSnackbarData?.let { data ->
                DagloSnackBar(
                    snackBarData = data,
                    type = type,
                )
            }
        }
    }
}

private fun createSnackbarData(message: String): SnackbarData = object : SnackbarData {
    override val visuals = object : SnackbarVisuals {
        override val actionLabel: String? = null
        override val duration = SnackbarDuration.Short
        override val message = message
        override val withDismissAction = false
    }

    override fun dismiss() {}
    override fun performAction() {}
}

@DagloPreview
@Composable
private fun PreviewDagloSnackBarDefault() {
    BasePreview(
        content = {
            DagloSnackBar(
                snackBarData = createSnackbarData("기본 메시지입니다"),
                type = SnackBarType.DEFAULT,
            )
        },
    )
}

@DagloPreview
@Composable
private fun PreviewDagloSnackBarError() {
    BasePreview(
        content = {
            DagloSnackBar(
                snackBarData = createSnackbarData("에러 메시지입니다"),
                type = SnackBarType.ERROR,
            )
        },
    )
}

@DagloPreview
@Composable
private fun PreviewDagloSnackBarWarning() {
    BasePreview(
        content = {
            DagloSnackBar(
                snackBarData = createSnackbarData("경고 메시지입니다"),
                type = SnackBarType.WARNING,
            )
        },
    )
}
