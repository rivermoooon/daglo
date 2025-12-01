package com.moonsu.assignment.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.moonsu.assignment.core.designsystem.R

@Composable
fun DagloImage(
    model: Any?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = model ?: R.drawable.ic_default,
        placeholder = painterResource(R.drawable.ic_default),
        contentScale = ContentScale.Fit,
        error = if (LocalInspectionMode.current) painterResource(R.drawable.ic_default) else null,
        contentDescription = null,
        modifier = modifier,
    )
}
