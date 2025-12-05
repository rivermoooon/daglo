package com.moonsu.assignment.core.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.gridTwoColumns(
    itemCount: Int,
    key: ((Int) -> Any)? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    content: @Composable RowScope.(Int) -> Unit,
) {
    val rowCount = (itemCount + 1) / 2

    items(
        count = rowCount,
        key = key,
    ) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement,
        ) {
            val first = rowIndex * 2
            val second = first + 1

            content(first)

            if (second < itemCount) {
                content(second)
            } else {
                Spacer(modifier = Modifier.weight(1f).height(0.dp))
            }
        }
    }
}
