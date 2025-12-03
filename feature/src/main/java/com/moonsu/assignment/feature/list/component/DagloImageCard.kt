package com.moonsu.assignment.feature.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.moonsu.assignment.core.designsystem.component.DagloImage
import com.moonsu.assignment.core.designsystem.component.preview.BasePreview
import com.moonsu.assignment.core.designsystem.component.preview.DagloPreview
import com.moonsu.assignment.core.designsystem.foundation.DagloColor
import com.moonsu.assignment.core.designsystem.foundation.DagloTheme

private val GradientOverlay = listOf(
    Color.Transparent,
    Color.Black.copy(alpha = 0.7f),
)

@Composable
fun DagloImageCard(
    imageUrl: String,
    name: String,
    status: String,
    gender: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 14.dp,
    elevation: Dp = 4.dp,
    height: Dp = 200.dp,
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }
    val isPreview = LocalInspectionMode.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shadow(elevation = elevation, shape = shape)
            .clip(shape)
            .background(DagloColor.Gray900)
            .clickableIfNotNull(onClick, interactionSource),
    ) {
        CharacterImage(
            imageUrl = imageUrl,
            isPreview = isPreview,
            modifier = Modifier.matchParentSize(),
        )

        GradientOverlayBox(
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        CharacterInfoOverlay(
            name = name,
            status = status,
            gender = gender,
            modifier = Modifier.align(Alignment.BottomStart),
        )
    }
}

@Composable
private fun CharacterImage(
    imageUrl: String,
    isPreview: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isPreview) {
        Box(
            modifier = modifier.background(DagloColor.Gray300),
        )
    } else {
        DagloImage(
            model = imageUrl,
            modifier = modifier,
        )
    }
}

@Composable
private fun GradientOverlayBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                brush = Brush.verticalGradient(colors = GradientOverlay),
            ),
    )
}

@Composable
private fun CharacterInfoOverlay(
    name: String,
    status: String,
    gender: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = name,
            style = DagloTheme.typography.titleMediumB,
            color = DagloColor.StaticWhite,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusIndicator(status = status)
            Text(
                text = "$status • $gender",
                style = DagloTheme.typography.bodySmallR,
                color = DagloColor.StaticWhite.copy(alpha = 0.8f),
            )
        }
    }
}

@Composable
private fun StatusIndicator(status: String) {
    val color = when (status.lowercase()) {
        "alive" -> DagloColor.Green500
        "dead" -> DagloColor.Red500
        else -> DagloColor.Gray500
    }

    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color),
    )
}

private fun Modifier.clickableIfNotNull(
    onClick: (() -> Unit)?,
    interactionSource: MutableInteractionSource,
): Modifier = if (onClick != null) {
    this.clickable(
        interactionSource = interactionSource,
        indication = ripple(bounded = true),
        onClick = onClick,
    )
} else {
    this
}

@DagloPreview
@Composable
private fun PreviewDagloImageCardAlive() {
    BasePreview {
        DagloImageCard(
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            name = "Rick Sanchez",
            status = "Alive",
            gender = "Male",
            onClick = {},
        )
    }
}

@DagloPreview
@Composable
private fun PreviewDagloImageCardDead() {
    BasePreview {
        DagloImageCard(
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
            name = "Morty Smith",
            status = "Dead",
            gender = "Male",
            onClick = {},
        )
    }
}

@DagloPreview
@Composable
private fun PreviewDagloImageCardUnknown() {
    BasePreview {
        DagloImageCard(
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
            name = "Summer Smith",
            status = "unknown",
            gender = "Female",
            onClick = {},
        )
    }
}
