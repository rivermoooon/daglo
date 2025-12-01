package com.moonsu.assignment.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moonsu.assignment.core.designsystem.component.preview.BasePreview
import com.moonsu.assignment.core.designsystem.component.preview.DagloPreview
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme
import kotlin.math.min

@Composable
fun DagloProgress(
    modifier: Modifier = Modifier,
    color: Color = DagloTheme.colors.primary,
    size: Dp = 24.dp,
    strokeWidth: Dp = 3.dp,
    speedMillis: Int = 1200,
    sweepAngle: Float = 90f,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "progress")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = speedMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation_anim",
    )

    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokePx = strokeWidth.toPx()
            val diameterPx = min(this.size.width, this.size.height)
            val arcSize = Size(diameterPx - strokePx, diameterPx - strokePx)
            val topLeft = Offset(
                (this.size.width - arcSize.width) / 2f,
                (this.size.height - arcSize.height) / 2f,
            )

            // 트랙
            drawArc(
                color = color.copy(alpha = 0.2f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )

            // 메인 회전
            drawArc(
                color = color,
                startAngle = rotation - 90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
        }
    }
}

@DagloPreview
@Composable
private fun PreviewProgress() {
    BasePreview(
        content = {
            DagloProgress()
        },
    )
}
